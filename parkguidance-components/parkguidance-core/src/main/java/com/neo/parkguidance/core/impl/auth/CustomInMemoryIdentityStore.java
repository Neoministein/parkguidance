package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.api.auth.CredentialsAuthenticationService;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.impl.auth.credential.OAuthCredential;
import com.neo.parkguidance.core.impl.auth.credential.TokenCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;

/**
 * The default implementation for authentication a user
 */
@Stateless
public class CustomInMemoryIdentityStore implements IdentityStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomInMemoryIdentityStore.class);

    @Inject
    CredentialsAuthenticationService authenticationService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        RegisteredUser user = retrieveUser(credential);
        if (user != null) {
            return new CredentialValidationResult(new RegisteredUserPrincipal(user.getId(), user.getUsername()), getUserPermissions(user));
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    public RegisteredUser retrieveUser(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential login = (UsernamePasswordCredential) credential;
            return authenticationService.authenticateUser(login.getCaller(),login.getPasswordAsString());
        }
        if (credential instanceof OAuthCredential) {
            OAuthCredential login = (OAuthCredential) credential;
            return authenticationService.oauth2UserAuthentication(login.getToken(), login.getType());
        }
        if (credential instanceof TokenCredentials) {
            TokenCredentials login = (TokenCredentials) credential;
            return authenticationService.authenticateUser(login.getToken());
        }
        LOGGER.error("Unsupported credential type [{}]", credential.getClass().getName());
        return null;
    }

    private HashSet<String> getUserPermissions(RegisteredUser user) {
        HashSet<String> permissionSet = new HashSet<>();
        for (Permission permission: user.getPermissions()) {
            permissionSet.add(permission.getName());
        }
        return permissionSet;
    }
}
