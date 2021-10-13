package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormModel;
import org.omnifaces.cdi.ViewScoped;


/**
 * The screen model for the StoredValueForm screen
 */
@ViewScoped
public class StoredValueFormModel extends AbstractFormModel<ConfigValue> {

    private String hiddenValue;

    public String getHiddenValue() {
        return hiddenValue;
    }

    public void setHiddenValue(String hiddenValue) {
        this.hiddenValue = hiddenValue;
    }
}
