package com.neo.parkguidance.web.admin.pages.configform;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.core.impl.config.ConfigType;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormController;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.Arrays;

import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

/**
 * The controller for the ConfigForm screen
 */
@RequestScoped
@Named(ConfigFormController.BEAN_NAME)
public class ConfigFormController extends AbstractFormController<Configuration> {

    public static final String BEAN_NAME = "configForm";

    @Inject ConfigFormModel model;

    @Inject ConfigFormFacade facade;

    @Override
    public void init() {
        if (!model.isInitialized()) {
            model.setConfigValue(new ConfigValue());
        }
        super.init();
    }

    @Override
    protected String getRedirectLocation() {
        return "admin/config-list";
    }

    @Override
    public void save() {
        try {
            StringBuilder msg = new StringBuilder(getModel().getEntity().getClass().getSimpleName()
                    + " " + getModel().getEntity().getKey());

            if (getModel().getPrimaryKey() == null) {
                if (isTypeSingle()) {
                    ConfigValue configValue = model.getConfigValue();
                    configValue.setKey(model.getEntity().getKey());
                    configValue.setDescription(model.getEntity().getDescription());
                    configValue.setConfiguration(model.getEntity());
                    model.getEntity().setConfigValues(Arrays.asList(model.getConfigValue()));
                }
                getFacade().create(model.getEntity());
                model.setPrimaryKey(model.getEntity().getId());
                msg.append(" created successfully");
            } else {
                if (isTypeSingle() &&
                        model.getEntity().getSingleValue().getHidden().booleanValue() &&
                        !StringUtils.isEmpty(model.getHiddenValue())) {
                    model.getEntity().getSingleValue().setValue(model.getHiddenValue());
                    model.setHiddenValue("");
                }
                facade.edit(getModel().getEntity());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
        model.setInitialized(false);
        init();
    }

    public boolean isTypeSingle() {
        return ConfigType.SINGLE.equals(model.getEntity().getType());
    }

    public void createConfigValue() {
        facade.createConfigValue(model.getEntity(), model.getConfigValue());
        model.setInitialized(false);
        init();
    }

    public void removeConfigValue() {
        facade.removeConfigValue(model.getEntity() ,model.getSelectedConfigValue());
        model.setInitialized(false);
        init();
    }

    public void onRowEdit(RowEditEvent<ConfigValue> event) {
        ConfigValue edit = event.getObject();
        if (edit.getHidden().booleanValue()) {
            edit.setValue(model.getHiddenValue());
        }
        facade.edit(model.getEntity());
        model.setInitialized(false);
        init();
    }

    @Override
    public ConfigFormModel getModel() {
        return model;
    }

    @Override
    protected ConfigFormFacade getFacade() {
        return facade;
    }
}
