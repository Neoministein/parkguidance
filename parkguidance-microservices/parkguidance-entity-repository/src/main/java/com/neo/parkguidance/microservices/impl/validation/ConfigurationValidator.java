package com.neo.parkguidance.microservices.impl.validation;

import com.neo.parkguidance.framework.entity.Configuration;
import com.neo.parkguidance.framework.impl.utils.StringUtils;

import javax.enterprise.context.RequestScoped;
import java.util.HashSet;
import java.util.Objects;

/**
 * Validates changes done to the {@link Configuration} entity
 */
@RequestScoped
public class ConfigurationValidator extends AbstractDatabaseEntityValidation<Configuration> {

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

    public void checkInvalidCharsInKey(String key) {
        if (key == null) {
            throw new com.neo.parkguidance.microservices.impl.validation.EntityValidationException("Key cannot be null");
        }
        if (StringUtils.isEmpty(key)) {
            throw new com.neo.parkguidance.microservices.impl.validation.EntityValidationException("A String Key cannot be empty");
        }
        if (key.replaceAll("[a-zA-Z\\d\\-_.]","").length() > 0) {
            throw new com.neo.parkguidance.microservices.impl.validation.EntityValidationException("Unsupported Character, Valid Characters include A-z, 0-9, '_', '-' and '.'");
        }
        if (uniqueValue(Configuration.C_KEY, key)) {
            throw new EntityValidationException("Key already exists");
        }
    }
}
