package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.RegisteredUser;

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

    public void uniqueUsername(String username) throws EntityValidationException {
        if (valueExists(RegisteredUser.C_USERNAME , username)) {
            throw new EntityValidationException("This username already exists");
        }
    }

    public void uniqueEmail(String email) throws EntityValidationException {
        if (valueExists(RegisteredUser.C_EMAIL , email)) {
            throw new EntityValidationException("This e-mail is already used");
        }
    }
}