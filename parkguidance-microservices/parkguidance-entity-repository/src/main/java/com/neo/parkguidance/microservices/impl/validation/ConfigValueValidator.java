package com.neo.parkguidance.microservices.impl.validation;

import com.neo.parkguidance.framework.entity.ConfigValue;
import com.neo.parkguidance.framework.entity.Configuration;
import com.neo.parkguidance.framework.impl.utils.StringUtils;

import javax.enterprise.context.RequestScoped;
import java.util.Objects;

/**
 * Validates changes done to the {@link ConfigValue} entity
 */
@RequestScoped
public class ConfigValueValidator extends AbstractDatabaseEntityValidation<ConfigValue> {

    @Override
    public boolean hasNothingChanged(ConfigValue entity) {
        ConfigValue originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if (!originalObject.getValue().equals(entity.getValue())) {
            return false;
        }
        if (!originalObject.getHidden().equals(entity.getHidden())) {
            return false;
        }
        return Objects.equals(originalObject.getDescription(), entity.getDescription());
    }

    protected void checkInvalidCharsInKey(String key) {
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
