package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.core.impl.utils.StringUtils;

import javax.ejb.Stateless;
import java.util.Objects;

/**
 * Validates changes done to the {@link ConfigValue} entity
 */
@Stateless
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
            throw new EntityValidationException("Key cannot be null");
        }
        if (StringUtils.isEmpty(key)) {
            throw new EntityValidationException("A String Key cannot be empty");
        }
        if (key.replaceAll("[a-zA-Z\\d\\-_.]","").length() > 0) {
            throw new EntityValidationException("Unsupported Character, Valid Characters include A-z, 0-9, '_', '-' and '.'");
        }
        if (uniqueValue(Configuration.C_KEY, key)) {
            throw new EntityValidationException("Key already exists");
        }
    }
}
