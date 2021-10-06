package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.api.auth.AuthenticationService;
import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

/**
 * Implementation of {@link AuthenticationService}
 */
@Stateless
public class DatabaseAuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAuthenticationServiceImpl.class);

    @Inject
    StoredValueService storedValueService;

    @Inject
    EntityDao<RegisteredUser> userDao;

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    EntityDao<Permission> permissionDao;

    @Inject
    EntityDao<UserToken> tokenDao;

    public RegisteredUser authenticateUser(String username, String password) {
        LOGGER.info("User credentials authentication attempt");

        String userPassword = new DigestUtils(SHA_224).digestAsHex(password.getBytes());
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

    public UserToken authenticateUser(String token) {
        LOGGER.info("User token authentication attempt");

        if (StringUtils.isEmpty(token)) {
            LOGGER.info("User token authentication failed");
            return null;
        }

        UserToken userToken = tokenDao.findOneByColumn(UserToken.C_KEY, token);
        if (userToken != null) {
            LOGGER.info("User token authentication found token [{}]", token);
            return userToken;
        }
        LOGGER.info("User authentication failed");
        return null;
    }

    public UserToken authenticateUser(String token, Collection<Permission> requiredPermissions) {
        UserToken userToken = authenticateUser(token);
        if (userToken != null && checkPermissions(userToken.getPermissions(), requiredPermissions)) {
            return userToken;
        }
        return null;
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
            for (String permissionName: this.storedValueService.getString(storedValueKey).replaceAll("\\s", "").split(",")) {
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
}
