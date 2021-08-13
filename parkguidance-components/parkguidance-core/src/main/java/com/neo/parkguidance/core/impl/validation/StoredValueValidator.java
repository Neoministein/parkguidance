package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.StoredValue;

import javax.ejb.Stateless;

/**
 * Validates changes done to the {@link StoredValue} entity
 */
@Stateless
public class StoredValueValidator extends AbstractDatabaseEntityValidation<StoredValue> {

    @Override
    public void validatePrimaryKey(Object primaryKey) {
        super.validatePrimaryKey(primaryKey);
        checkInvalidCharsInPrimaryKey((String) primaryKey);
    }

    protected void checkInvalidCharsInPrimaryKey(String primaryKey) {
        if (primaryKey.replaceAll("[a-zA-Z\\d\\-_.]","").length() > 0) {
            throw new IllegalArgumentException("Unsupported Character, Valid Characters include A-z, '_' and '-'.");
        }
    }
}
