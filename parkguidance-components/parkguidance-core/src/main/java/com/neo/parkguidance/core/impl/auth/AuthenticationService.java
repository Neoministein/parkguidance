package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

@Stateless
public class AuthenticationService {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationService.class);

    @Inject
    AbstractEntityDao<RegisteredUser> userDao;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    public RegisteredUser authenticateUser(String username, String password) {
        LOGGER.info("User credentials authentication attempt");

        String userPassword = new DigestUtils(SHA_224).digestAsHex(password.getBytes());
        RegisteredUser user = userDao.findOneByColumn(RegisteredUser.C_USERNAME, username);

        if(user != null) {
            if (user.getPassword().equals(userPassword)) {
                LOGGER.info("User credentials authentication password acknowledged with account [{}]", user.getUsername());

                return user;
            } else {
                LOGGER.info("User credentials authentication on non existent account [{}] ", user.getUsername());
            }
        } else {
            LOGGER.info("User credentials authentication on non existent account [{}] ", username);
        }
        return null;
    }

    public RegisteredUser authenticateUser(String username, String password, Collection<Permission> requiredPermissions) {
        return checkPermissions(authenticateUser(username, password), requiredPermissions);
    }

    public RegisteredUser authenticateUser(String token) {
        LOGGER.info("User token authentication attempt");

        if (StringUtils.isEmpty(token)) {
            LOGGER.info("User token authentication failed");
            return null;
        }

        RegisteredUser user = userDao.findOneByColumn(RegisteredUser.C_TOKEN, token);
        if (user != null) {
            LOGGER.info("User token authentication found user [{}]", user.getUsername());
            return user;
        }
        LOGGER.info("User authentication failed");
        return null;
    }

    public RegisteredUser authenticateUser(String token, Collection<Permission> requiredPermissions) {
        return checkPermissions(authenticateUser(token), requiredPermissions);
    }

    protected RegisteredUser checkPermissions(RegisteredUser user, Collection<Permission> requiredPermissions) {
        if (user == null) {
            return null;
        }

        if (user.getPermissions().containsAll(requiredPermissions)) {
            LOGGER.info("User authentication required permissions found");
            return user;
        }

        LOGGER.info("User authentication required permissions not found");
        return null;
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
}
