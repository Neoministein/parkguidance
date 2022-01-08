package com.neo.parkguidance.ms.user.api.security;

import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.impl.security.credentail.RegisteredCredentials;
import com.neo.parkguidance.ms.user.impl.security.credentail.TokenCredentials;

/**
 * AuthenticationService is a interface defining authentication
 */
public interface CredentialsManagerService {

    /**
     * Checks if a username and password match an existing user
     *
     * @param userCredentials userCredentials
     * @return the {@link RegisteredUser} if found else null
     */
    RegisteredUser retrieveUser(RegisteredCredentials userCredentials);

    /**
     * Checks if the token is valid and attached to valid user
     *
     * @param tokenCredentials tokenCredentials
     * @return the {@link RegisteredUser} if the token is valid else null
     */
    UserToken retrieveUser(TokenCredentials tokenCredentials);
}
