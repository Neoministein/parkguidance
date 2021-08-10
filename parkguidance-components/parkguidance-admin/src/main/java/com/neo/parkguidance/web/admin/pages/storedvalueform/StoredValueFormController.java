package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.StoredValue;
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
public class StoredValueFormController extends AbstractFormController<StoredValue> {

    public static final String BEAN_NAME = "storedValueForm";

    @Inject
    StoredValueFormModel modelImpl;

    @Inject
    StoredValueFormFacade facadeImpl;

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
                getFacade().create(modelImpl.getEntity());
                msg.append(" created successfully");
            } else {
                facadeImpl.edit(getModel().getEntity(), modelImpl.getHiddenValue());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
    }

    public StoredValueFormModel getImplModel() {
        return modelImpl;
    }
}
