package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.utils.RandomString;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Objects;

@Stateless
public class RegisteredUserValidator extends AbstractDatabaseEntityValidation<RegisteredUser> {

    @Override
    public boolean hasNothingChanged(RegisteredUser entity) {
        RegisteredUser originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }
        if (!Objects.equals(entity.getUsername(), originalObject.getUsername())) {
            return false;
        }

        if (!Objects.equals(entity.getEmail(), originalObject.getEmail())) {
            return false;
        }

        if (!Objects.equals(entity.getPassword(), originalObject.getPassword())) {
            return false;
        }

        if (!Objects.equals(entity.getDeactivated(), originalObject.getDeactivated())) {
            return false;
        }

        if (!Objects.equals(entity.getDeactivatedOn(), originalObject.getDeactivatedOn())) {
            return false;
        }

        if (!Objects.equals(entity.getCreatedOn(), originalObject.getCreatedOn())) {
            return false;
        }

        if (entity.getPermissions().size() != originalObject.getPermissions().size()) {
            return false;
        }

        if (!new HashSet<>(entity.getPermissions()).equals(new HashSet<>(originalObject.getPermissions()))) {
            return false;
        }

        if (entity.getTokens().size() != originalObject.getTokens().size()) {
            return false;
        }

        return new HashSet<>(entity.getTokens()).equals(new HashSet<>(originalObject.getTokens()));
    }

    public void newUniqueKey(UserToken userToken) {
        String accessKey;
        do {
            accessKey = new RandomString().nextString();
        } while (valueExists(UserToken.C_KEY, accessKey));
        userToken.setKey(accessKey);
    }
}