package com.neo.parkguidance.web.admin.security;

import com.neo.parkguidance.core.api.auth.AuthenticationService;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;

@ApplicationScoped
public class CustomInMemoryIdentityStore implements IdentityStore {

    @Inject
    AuthenticationService authenticationService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;

        RegisteredUser user = authenticationService.authenticateUser(login.getCaller(),login.getPasswordAsString());
        if (user != null) {
            return new CredentialValidationResult(user.getUsername(), getUserPermissions(user));
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    private HashSet<String> getUserPermissions(RegisteredUser user) {
        HashSet<String> permissionSet = new HashSet<>();
        for (Permission permission: user.getPermissions()) {
            permissionSet.add(permission.getName());
        }
        return permissionSet;
    }
}
