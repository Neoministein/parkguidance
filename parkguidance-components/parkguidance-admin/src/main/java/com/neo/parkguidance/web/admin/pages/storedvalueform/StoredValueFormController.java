package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormController;
import org.omnifaces.util.Messages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

/**
 * The controller for the StoredValueForm screen
 */
@RequestScoped
@Named(StoredValueFormController.BEAN_NAME)
public class StoredValueFormController extends AbstractFormController<ConfigValue> {

    public static final String BEAN_NAME = "storedValueForm";

    @Inject
    StoredValueFormModel model;

    @Inject
    StoredValueFormFacade facade;

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected String getRedirectLocation() {
        return "admin/storedValues-form";
    }

    @Override
    public void save() {
        try {
            StringBuilder msg = new StringBuilder(getModel().getEntity().getClass().getSimpleName()
                    + " " + getModel().getEntity().getPrimaryKey());

            if (getModel().getPrimaryKey() == null) {
                getFacade().create(model.getEntity());
                msg.append(" created successfully");
            } else {
                facade.edit(getModel().getEntity(), model.getHiddenValue());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
    }

    @Override
    public StoredValueFormModel getModel() {
        return model;
    }

    @Override
    protected StoredValueFormFacade getFacade() {
        return facade;
    }
}
