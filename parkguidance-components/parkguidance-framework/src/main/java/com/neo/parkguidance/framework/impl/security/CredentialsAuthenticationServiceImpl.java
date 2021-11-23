package com.neo.parkguidance.framework.impl.security;

import com.neo.parkguidance.framework.api.config.ConfigService;
import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.api.security.CredentialsAuthenticationService;
import com.neo.parkguidance.framework.api.security.oauth2.OAuth2Client;
import com.neo.parkguidance.framework.api.security.token.TokenService;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.entity.Permission;
import com.neo.parkguidance.framework.entity.RegisteredUser;
import com.neo.parkguidance.framework.entity.UserCredentials;
import com.neo.parkguidance.framework.impl.security.exception.UnsupportedOAuth2Provider;
import com.neo.parkguidance.framework.impl.security.oauth2.OAuth2ClientObject;
import com.neo.parkguidance.framework.impl.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.*;

/**
 * Implementation of {@link CredentialsAuthenticationService}
 */
@RequestScoped
public class CredentialsAuthenticationServiceImpl implements CredentialsAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialsAuthenticationServiceImpl.class);

    @Inject
    ConfigService configService;

    @Inject
    EntityDao<RegisteredUser> userDao;

    @Inject
    EntityDao<UserCredentials> userCredentialsDao;

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    EntityDao<Permission> permissionDao;

    @Inject
    TokenService tokenService;

    @Inject
    Instance<OAuth2Client> oAuth2Clients;

    public RegisteredUser authenticateUser(String username, String password) {
        LOGGER.info("User credentials authentication attempt");

        String userPassword = StringUtils.hashString(password);
        RegisteredUser user = userDao.findOneByColumn(RegisteredUser.C_USERNAME, username);

        if(user != null) {
            if (user.getPassword().equals(userPassword)) {
                if (!Boolean.TRUE.equals(user.getDeactivated())) {
                    LOGGER.info("User credentials authentication password acknowledged with account [{}]", user.getUsername());
                    return user;
                }
                LOGGER.info("User credentials authentication password acknowledged but account is disabled with account [{}]", user.getUsername());
            } else {
                LOGGER.info("User credentials authentication on non existent account failed due to wrong password [{}] ", user.getUsername());
            }
        } else {
            LOGGER.info("User credentials authentication on non existent account [{}] ", username);
        }
        return null;
    }

    public RegisteredUser authenticateUser(String username, String password, Collection<Permission> requiredPermissions) {
        RegisteredUser registeredUser = authenticateUser(username, password);
        if (registeredUser != null && checkPermissions(registeredUser.getPermissions(), requiredPermissions)) {
            return registeredUser;
        }
        return null;
    }

    public RegisteredUser authenticateUser(String token) {
        return tokenService.validateToken(token).getRegisteredUser();
    }

    public RegisteredUser authenticateUser(String token, Collection<Permission> requiredPermissions) {
        RegisteredUser userToken = authenticateUser(token);
        if (userToken != null && checkPermissions(userToken.getPermissions(), requiredPermissions)) {
            return userToken;
        }
        return null;
    }

    @Override
    public RegisteredUser oauth2UserAuthentication(String token, String provider) {
        OAuth2ClientObject clientObject = findDedicatedOAuth2Client(provider).verifyToken(token);
        if (clientObject == null) {
            return null;
        }

        UserCredentials userCredentials = findCredentials(clientObject.getClientId(), provider);
        if (userCredentials != null) {
            return userCredentials.getRegisteredUser();
        }

        RegisteredUser existingUser = userDao.findOneByColumn(RegisteredUser.C_EMAIL, clientObject.getEmail());
        if (existingUser != null) {
            addNewCredentials(existingUser, clientObject, provider);
            userDao.edit(existingUser);
            return existingUser;
        }

        RegisteredUser newUser = new RegisteredUser();
        newUser.setEmail(clientObject.getEmail());
        newUser.setUsername(clientObject.getUsername());
        addNewCredentials(newUser, clientObject, provider);
        userDao.create(newUser);
        return newUser;
    }

    protected boolean checkPermissions(Collection<Permission> userPermissions, Collection<Permission> requiredPermissions) {
        if (userPermissions.containsAll(requiredPermissions)) {
            LOGGER.info("User authentication required permissions found");
            return true;
        }

        LOGGER.info("User authentication required permissions {} not found", requiredPermissions.removeAll(userPermissions));
        return false;
    }

    public ParkingGarage authenticateGarage(String accessKey) {
        LOGGER.info("ParkingGarage accessKey authentication attempt");
        ParkingGarage parkingGarage = parkingGarageDao.findOneByColumn(ParkingGarage.C_ACCESS_KEY, accessKey);

        if (parkingGarage != null) {
            LOGGER.info("ParkingGarage accessKey authentication success");
        } else {
            LOGGER.info("ParkingGarage accessKey authentication failed");
        }

        return parkingGarage;
    }

    public Set<Permission> getRequiredPermissions(String storedValueKey) {
        Set<Permission> permissions = new HashSet<>();

        try {
            for (String permissionName: this.configService.getString(storedValueKey).replaceAll("\\s", "").split(",")) {
                Permission permission = permissionDao.findOneByColumn(Permission.C_NAME, permissionName);
                if (permission != null) {
                    permissions.add(permission);
                }
            }

            if (permissions.isEmpty()) {
                throw new IllegalStateException("No internal permission specified for using service");
            }

            return permissions;
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("No internal permission specified for using service");
        }
    }

    protected OAuth2Client findDedicatedOAuth2Client(String provider) {
        for (OAuth2Client oAuth2Client: oAuth2Clients) {
            if (oAuth2Client.getProvider().equals(provider)) {
                return oAuth2Client;
            }
        }
        LOGGER.error("OAuth2 Provider [{}] is not implemented", provider);
        throw new UnsupportedOAuth2Provider(provider);
    }

    public UserCredentials findCredentials(String clientId, String provider) {
        Map<String, Object> columnData = new HashMap<>();
        columnData.put(UserCredentials.C_CLIENT_ID, clientId);
        columnData.put(UserCredentials.C_TYPE, provider);

        return userCredentialsDao.findOneByColumn(columnData);
    }

    protected void addNewCredentials(RegisteredUser registeredUser, OAuth2ClientObject clientObject, String provider) {
        UserCredentials newCredentials = new UserCredentials();
        newCredentials.setClientId(clientObject.getClientId());
        newCredentials.setType(provider);
        newCredentials.setRegisteredUser(registeredUser);
        registeredUser.getUserCredentials().add(newCredentials);
        if (StringUtils.isEmpty(registeredUser.getPassword())) {
            registeredUser.setPicture(clientObject.getPicture());
        }
    }
}
