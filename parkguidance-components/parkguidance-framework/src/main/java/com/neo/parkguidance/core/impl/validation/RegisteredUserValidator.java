package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.RegisteredUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Pattern;

@Stateless
public class RegisteredUserValidator extends AbstractDatabaseEntityValidation<RegisteredUser> {

    public static final int HASH_LENGTH = 128;
    public static final String HASH_TYPE = MessageDigestAlgorithms.SHA3_512;

    public static final String INVALID_PASSWORD = "The password must contain"
            + ", a digit [0-9]"
            + ", a lower case character [a-z]"
            + ", a upper case character [A-Z]"
            + ", a special character like ! @ # & ( )"
            + ", of at least 8 and a maximum of 64 characters";

    // digit + lowercase char + uppercase char + punctuation + symbol
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

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

    public void validatePassword(String password) {
        if (password == null || (password.length() != HASH_LENGTH && !pattern.matcher(password).matches())) {
            throw new EntityValidationException(INVALID_PASSWORD);
        }
    }

    public String hashPassword(String password) {
        if (password.length() != HASH_LENGTH) {
            return new DigestUtils(HASH_TYPE).digestAsHex(password.getBytes());
        }
        return password;
    }
}