package com.neo.parkguidance.web.admin.security;

import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;
import java.util.List;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

@ApplicationScoped
public class CustomInMemoryIdentityStore implements IdentityStore {

    private static final Logger LOGGER = LogManager.getLogger(CustomInMemoryIdentityStore.class);

    @Inject
    AbstractEntityDao<RegisteredUser> dao;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;

        LOGGER.info("Login attempt");
        String userPassword = new DigestUtils(SHA_224).digestAsHex(login.getPasswordAsString().getBytes());
        RegisteredUser dbUser = lookUpDBUser(login.getCaller());

        if(dbUser != null) {
            if (dbUser.getPassword().equals(userPassword)) {
                LOGGER.info("Login success with account [{}]", dbUser.getUsername());

                return new CredentialValidationResult(dbUser.getUsername(), getUserPermissions(dbUser));
            } else {
                LOGGER.info("Login attempt on non existent account [{}] ", dbUser.getUsername());
            }
        } else {
            LOGGER.info("Login attempt on non existent account [{}] ", login.getCaller());
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    private RegisteredUser lookUpDBUser(String username){
        List<RegisteredUser> dbLookup = dao.findByColumn(RegisteredUser.C_USERNAME,username);

        if(!dbLookup.isEmpty()) {
            return dbLookup.get(0);
        } else {
            return null;
        }
    }

    private HashSet<String> getUserPermissions(RegisteredUser user) {
        HashSet<String> permissionSet = new HashSet<>();
        for (Permission permission: user.getPermissions()) {
            permissionSet.add(permission.getName());
        }
        return permissionSet;
    }
}
