package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.api.auth.TokenService;
import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Inject
    EntityDao<UserToken> userTokenDao;

    @Inject
    EntityDao<RegisteredUser> registeredUserDao;

    @Override
    public UserToken validateToken(String token) {
        LOGGER.info("User token authentication attempt");

        if (StringUtils.isEmpty(token)) {
            LOGGER.info("User token authentication failed");
            return null;
        }

        UserToken userToken = userTokenDao.findOneByColumn(UserToken.C_KEY, token);
        if (userToken != null) {
            if (userToken.getExpirationDate() != null && new Date().after(userToken.getExpirationDate())) {
                userTokenDao.remove(userToken);
                return null;
            }
            if (TokenType.ONE_TIME.equals(userToken.getType())) {
                userTokenDao.remove(userToken);
            }
            LOGGER.info("User token authentication found token [{}]", token);
            return userToken;
        }
        LOGGER.info("User authentication failed");
        return null;
    }

    @Override
    public void inValidateToken(long clientId, String token) {
        this.inValidateToken(registeredUserDao.find(clientId), token);
    }

    @Override
    public void inValidateToken(RegisteredUser user, String token) {
        UserToken tokenToInvalidate = null;
        for (UserToken userToken: user.getTokens()) {
            if (userToken.getKey().equals(token)) {
                tokenToInvalidate = userToken;
                break;
            }
        }

        if (tokenToInvalidate == null) {
            LOGGER.warn("User {} does not have the token ", token);
            return;
        }
        user.getTokens().remove(tokenToInvalidate);
        registeredUserDao.edit(user);
    }

    @Override
    public UserToken generateToken(long userId, TokenType tokenType) {
        return this.generateToken(userId, new Date(System.currentTimeMillis() + 1000 * 60 * 60), tokenType, null);
    }

    @Override
    public UserToken generateToken(long userId, Date expirationDate, TokenType type) {
        return this.generateToken(userId, expirationDate, type, null);
    }

    @Override
    public UserToken generateToken(long userId, Date expirationDate, TokenType type, String description) {
        RegisteredUser registeredUser = registeredUserDao.find(userId);

        if (registeredUser == null) {
            return null;
        }

        return this.generateToken(registeredUser, expirationDate, type, description);
    }

    @Override
    public UserToken generateToken(RegisteredUser registeredUser, Date expirationDate, TokenType type) {
        return this.generateToken(registeredUser, expirationDate, type, null);
    }

    @Override
    public UserToken generateToken(RegisteredUser registeredUser, TokenType type) {
        return this.generateToken(registeredUser, null ,type, null);
    }


    @Override
    public UserToken generateToken(RegisteredUser registeredUser, Date expirationDate, TokenType type, String description) {
        UserToken userToken = new UserToken();
        userToken.setType(type);
        userToken.setExpirationDate(expirationDate);
        userToken.setName(description);

        userTokenDao.create(userToken);
        userToken.setRegisteredUser(registeredUser);
        registeredUser.getTokens().add(userToken);

        registeredUserDao.edit(registeredUser);
        return userToken;
    }
}
