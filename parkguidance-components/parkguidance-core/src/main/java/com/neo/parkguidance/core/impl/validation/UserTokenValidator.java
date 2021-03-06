package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.utils.RandomString;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Objects;

@Stateless
public class UserTokenValidator extends AbstractDatabaseEntityValidation<UserToken> {

    @Override
    public boolean hasNothingChanged(UserToken entity) {
        UserToken originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if (!Objects.equals(entity.getName(), originalObject.getName())) {
            return false;
        }

        if (!Objects.equals(entity.getKey(), originalObject.getKey())) {
            return false;
        }

        if (entity.getPermissions().size() != originalObject.getPermissions().size()) {
            return false;
        }

        return new HashSet<>(entity.getPermissions()).equals(new HashSet<>(originalObject.getPermissions()));
    }

    public void newUniqueKey(UserToken userToken) {
        String accessKey;
        do {
            accessKey = new RandomString().nextString();
        } while (valueExists(UserToken.C_KEY, accessKey));
        userToken.setKey(accessKey);
    }
}
