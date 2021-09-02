package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.StoredValue;

import javax.ejb.Stateless;

/**
 * Validates changes done to the {@link StoredValue} entity
 */
@Stateless
public class StoredValueValidator extends AbstractDatabaseEntityValidation<StoredValue> {

    @Override
    public void validatePrimaryKey(Object primaryKey) throws EntityValidationException {
        super.validatePrimaryKey(primaryKey);
        checkInvalidCharsInPrimaryKey((String) primaryKey);
    }

    @Override
    public boolean compareValues(StoredValue entity) {
        StoredValue originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if(!originalObject.getKey().equals(entity.getKey())) {
            return false;
        }
        return originalObject.getKey().equals(entity.getValue());
    }

    protected void checkInvalidCharsInPrimaryKey(String primaryKey) throws EntityValidationException {
        if (primaryKey.replaceAll("[a-zA-Z\\d\\-_.]","").length() > 0) {
            throw new EntityValidationException("Unsupported Character, Valid Characters include A-z, '_' and '-'.");
        }
    }
}
