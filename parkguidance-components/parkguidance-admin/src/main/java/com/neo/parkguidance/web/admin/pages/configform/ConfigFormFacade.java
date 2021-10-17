package com.neo.parkguidance.web.admin.pages.configform;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormFacade;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.primefaces.PrimeFaces;

import javax.ejb.Stateless;

/**
 * The screen facade for the ConfigForm screen
 */
@Stateless
public class ConfigFormFacade extends AbstractFormFacade<Configuration> {

    @Override
    public Configuration newEntity() {
        return new Configuration();
    }

    public void createConfigValue(Configuration configuration, ConfigValue configValue) {
        configuration.getConfigValues().add(configValue);
        configValue.setConfiguration(configuration);
        dao.edit(configuration);
    }

    public void removeConfigValue(Configuration configuration, ConfigValue configValue) {
        removeConfigValueFromConfiguration(configuration, configValue);
        dao.edit(configuration);
        Utils.addDetailMessage("Removed configValue: " + configValue.getKey());
        PrimeFaces.current().ajax().update(":tokenForm:userTokenTable");
    }

    @SuppressWarnings("java:S5413") //I break out of the loop therefore I don't need to edit it
    private void removeConfigValueFromConfiguration(Configuration configuration, ConfigValue configValue) {
        for (int i = 0; i < configuration.getConfigValues().size(); i++) {
            if (configuration.getConfigValues().get(i).getId().equals(configValue.getId())) {
                configuration.getConfigValues().remove(i);
                return;
            }
        }
    }
}
