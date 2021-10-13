package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.Configuration;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Objects;

/**
 * Validates changes done to the {@link Configuration} entity
 */
@Stateless
public class ConfigurationValidator extends AbstractDatabaseEntityValidation<Configuration> {

    @Override
    public void validatePrimaryKey(Object primaryKey) {
        super.validatePrimaryKey(primaryKey);
        checkInvalidCharsInPrimaryKey((String) primaryKey);
    }

    @Override
    public boolean hasNothingChanged(Configuration entity) {
        Configuration originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if (!originalObject.getType().equals(entity.getType())) {
            return false;
        }

        if (!originalObject.getComponent().equals(entity.getComponent())) {
            return false;
        }
        if (!Objects.equals(originalObject.getDescription(), entity.getDescription())) {
            return false;
        }
        return new HashSet<>(originalObject.getConfigValues()).equals(new HashSet<>(entity.getConfigValues()));
    }

    protected void checkInvalidCharsInPrimaryKey(String primaryKey) {
        if (primaryKey.replaceAll("[a-zA-Z\\d\\-_.]","").length() > 0) {
            throw new EntityValidationException("Unsupported Character, Valid Characters include A-z, 0-9, '_', '-' and '.'");
        }
    }
}
