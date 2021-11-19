package com.neo.parkguidance.web.admin.pages.configform;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormModel;
import org.omnifaces.cdi.ViewScoped;

/**
 * The screen model for the ConfigForm screen
 */
@ViewScoped
public class ConfigFormModel extends AbstractFormModel<Configuration> {

    private String hiddenValue;
    private ConfigValue configValue;
    private ConfigValue selectedConfigValue;

    public String getHiddenValue() {
        return hiddenValue;
    }

    public void setHiddenValue(String hiddenValue) {
        this.hiddenValue = hiddenValue;
    }

    public ConfigValue getConfigValue() {
        return configValue;
    }

    public void setConfigValue(ConfigValue configValue) {
        this.configValue = configValue;
    }

    public ConfigValue getSelectedConfigValue() {
        return selectedConfigValue;
    }

    public void setSelectedConfigValue(ConfigValue selectedConfigValue) {
        this.selectedConfigValue = selectedConfigValue;
    }
}
