package com.neo.parkguidance.core.api.auth;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;

import java.util.Collection;
import java.util.Set;

/**
 * AuthenticationService is a interface defining authentication
 */
public interface AuthenticationService {

    /**
     * Checks if a username and password match an existing user
     *
     * @param username the users username
     * @param password the users password
     * @return the {@link RegisteredUser} if found else null
     */
    RegisteredUser authenticateUser(String username, String password);

    /**
     * Checks if a username and password match an existing user and if that users has the required permissions
     *
     * @param username the users username
     * @param password the users password
     * @param requiredPermissions the permissions required to authenticate
     * @return the {@link RegisteredUser} if found and has requiredPermissions else null
     */
    RegisteredUser authenticateUser(String username, String password, Collection<Permission> requiredPermissions);

    /**
     * Checks if the token is attached to valid user
     *
     * @param token the token
     * @return the {@link RegisteredUser} if the token is valid else null
     */
    UserToken authenticateUser(String token);

    /**
     * Checks if the token is attached to valid user and the user has the required permissions
     *
     * @param token the token
     * @param requiredPermissions the permissions required to authenticate
     * @return the {@link RegisteredUser} if found and has requiredPermissions else null
     */
    UserToken authenticateUser(String token, Collection<Permission> requiredPermissions);

    /**
     * Checks if accessKey is valid and linked to a {@link ParkingGarage}
     *
     * @param accessKey the accessKey of the {@link ParkingGarage}
     * @return the {@link ParkingGarage} if found else null
     */
    ParkingGarage authenticateGarage(String accessKey);

    /**
     * Gets {@link Permission} list which are defined from a {@link com.neo.parkguidance.core.entity.StoredValue} <br>
     * throws an {@link IllegalStateException} if no valid permissions are found
     *
     * @param storedValueKey the key of a {@link com.neo.parkguidance.core.entity.StoredValue}
     * @retur a {@link Set<Permission>} if a stored Value exists
     */
    Set<Permission> getRequiredPermissions(String storedValueKey);
}
