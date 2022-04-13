package com.neo.parkguidance.ms.user.impl.security;

import com.neo.parkguidance.common.impl.util.StringUtils;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import com.neo.parkguidance.ms.user.api.security.CredentialsManagerService;
import com.neo.parkguidance.ms.user.impl.entity.*;
import com.neo.parkguidance.ms.user.impl.security.credentail.AbstractLoginCredentials;
import com.neo.parkguidance.ms.user.impl.security.credentail.RegisteredCredentials;
import com.neo.parkguidance.ms.user.impl.security.credentail.TokenCredentials;
import com.neo.parkguidance.ms.user.impl.utils.CredentialsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import java.util.*;

/**
 * Implementation of {@link CredentialsManagerService}
 */
@RequestScoped
public class CredentialsManagerServiceImpl implements CredentialsManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialsManagerServiceImpl.class);

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private static final Map<String, Boolean> ATTEMPT_SORT_ORDER = Map.of(
            DataBaseEntity.C_ID, false
    );

    @Inject
    EntityDao<RegisteredUser> userDao;

    @Inject
    EntityDao<UserToken> userTokenDao;

    @Inject
    EntityDao<LoginAttempt> loginAttemptDao;

    @Override
    public RegisteredUser retrieveUser(RegisteredCredentials credentials) {
        LOGGER.info("Retrieving a registered user");
        if (StringUtils.isEmpty(credentials.getIdentification()) || StringUtils.isEmpty(credentials.getPassword())) {
            LOGGER.info("Retrieving registered user failed a provided string is null");
            return null;
        }

        RegisteredUser registeredUser = userDao.findOneByColumn(RegisteredUser.C_USERNAME, credentials.getIdentification());

        if (registeredUser == null) {
            registeredUser = userDao.findOneByColumn(RegisteredUser.C_EMAIL, credentials.getIdentification());
        }

        if(registeredUser != null) {
            String userPassword = CredentialsUtils.hashPassword(credentials.getPassword(), registeredUser.getSalt());

            if (registeredUser.getPassword().equals(userPassword)) {
                LOGGER.info("Retrieval success, password acknowledged with account [{}]", registeredUser.getUsername());
                createLoginAttempt(credentials,registeredUser, false);
                return registeredUser;
            } else {
                LOGGER.info("Retrieval on existent account failed due to wrong password [{}] ", registeredUser.getUsername());
            }
        } else {
            LOGGER.info("Retrieval failed due to a non existent account [{}] ", credentials.getIdentification());
        }
        createLoginAttempt(credentials,registeredUser, true);
        return null;
    }

    public UserToken retrieveUser(TokenCredentials token) {
        LOGGER.info("Retrieving user token");

        if (StringUtils.isEmpty(token.getToken())) {
            LOGGER.info("Retrieving user token failed provided string is null");
            return null;
        }

        UserToken userToken = userTokenDao.findOneByColumn(UserToken.C_KEY, token.getToken());
        if (userToken != null) {
            if (userToken.getExpirationDate() != null && new Date().after(userToken.getExpirationDate())) {
                try {
                    userTokenDao.remove(userToken);
                } catch (RollbackException ex) {
                    LOGGER.warn("Unable to remove expired token {}", userToken.getId(), ex);
                }
                return null;
            }

            LOGGER.info("Retrieval success, token acknowledged [{}]", token);
            if (TokenType.ONE_TIME.equals(userToken.getType())) {
                LOGGER.debug("Retrieved token is designated as a one time use");
                try {
                    userTokenDao.remove(userToken);
                } catch (RollbackException e) {
                    LOGGER.error("Unable to remove ");
                    return null;
                }
            }

            return userToken;
        }
        LOGGER.info("User authentication failed");
        return null;
    }

    protected void createLoginAttempt(AbstractLoginCredentials loginCredentials, RegisteredUser registeredUser, boolean failed) {
        LoginAttempt loginAttempt = new LoginAttempt(loginCredentials.getTime(), loginCredentials.getIpAddress(),
                failed, loginCredentials.getEndPoint(), registeredUser);

        if (failed && registeredUser != null && UserStatus.OK.equals(registeredUser.getUserStatus())) {
            checkForToManyAuth(registeredUser);
        }
        try {
            loginAttemptDao.create(loginAttempt);
        } catch (RollbackException ex) {
            LOGGER.warn("Unable to create an login attempt for user", ex);
        }
    }

    protected void checkForToManyAuth(RegisteredUser registeredUser) {
        Map<String, Object> column = new HashMap<>();
        column.put(RegisteredUser.TABLE_NAME, registeredUser);
        List<LoginAttempt> attempts = loginAttemptDao.findByColumn(column, ATTEMPT_SORT_ORDER,0, MAX_FAILED_ATTEMPTS);
        if (MAX_FAILED_ATTEMPTS <= attempts.size()) {
            boolean hasSuccessfulLogin = false;
            for (int i = 0; i < MAX_FAILED_ATTEMPTS; i++) {
                if (!attempts.get(i).getFailed().booleanValue()) {
                    hasSuccessfulLogin = true;
                }
            }
            if (!hasSuccessfulLogin) {
                registeredUser.setUserStatus(UserStatus.LOCKET_TO_MANY_FAILED_AUTH);
                try {
                    userDao.edit(registeredUser);
                } catch (RollbackException ex) {
                    LOGGER.error("Unable to lock user {}", registeredUser.getId(), ex);
                }

            }
        }
    }
}
