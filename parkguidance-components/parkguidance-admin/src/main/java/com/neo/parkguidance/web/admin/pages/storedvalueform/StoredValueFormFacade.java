package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormFacade;

import javax.ejb.Stateless;

/**
 * The screen facade for the StoredValueForm screen
 */
@Stateless
public class StoredValueFormFacade extends AbstractFormFacade<ConfigValue> {

    @Override
    public ConfigValue newEntity() {
        return new ConfigValue();
    }

    public void edit(ConfigValue storedValue, String valueHidden) {
        if (Boolean.TRUE.equals(storedValue.getHidden())) {
            storedValue.setValue(valueHidden);
        }
        super.edit(storedValue);
    }

    @Override
    public void create(ConfigValue storedValue) {
        if (storedValue.getHidden() == null) {
            storedValue.setHidden(Boolean.FALSE);
        }
        super.create(storedValue);
    }
}
