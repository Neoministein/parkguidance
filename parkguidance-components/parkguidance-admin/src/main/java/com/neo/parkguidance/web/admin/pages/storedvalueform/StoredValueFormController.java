package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.StoredValue;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.github.adminfaces.template.util.Assert.has;
import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

/**
 * The controller for the StoredValueForm screen
 */
@RequestScoped
@Named(StoredValueFormController.BEAN_NAME)
public class StoredValueFormController {

    public static final String BEAN_NAME = "storedValueForm";

    @Inject
    StoredValueFormFacade facade;

    @Inject
    StoredValueFormModel model;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        if (has(model.getKey())) {
            model.setItem(facade.find(model.getKey()));
        } else {
            model.setItem(new StoredValue());
        }
    }

    public void remove() {
        if (facade.remove(model.getItem())) {
            addDetailMessage("Parking Garage " + model.getItem().getKey()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("/admin/parkingGarage-list.xhtml");
        }
    }

    public void save() {
        try {
            StringBuilder msg = new StringBuilder("Parking Garage " + model.getItem().getKey());

            if (model.getKey() == null) {
                facade.create(model.getItem());
                msg.append(" created successfully");
            } else {
                facade.edit(model.getItem(), getModel().getHiddenValue());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
    }

    public void clear() {
        model.setItem(new StoredValue());
        model.setKey(null);
    }

    public boolean isNew() {
        return model.getItem() == null || model.getItem().getKey() == null;
    }

    public StoredValueFormModel getModel() {
        return model;
    }
}
