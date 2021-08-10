package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

/**
 * The controller for the GarageForm screen
 */
@RequestScoped
@Named(value = GarageFormController.BEAN_NAME)
public class GarageFormController {

    public static final String BEAN_NAME = "garageForm";

    @Inject
    GarageFormModel model;

    @Inject
    GarageFormFacade facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        if (has(model.getKey())) {
            model.setItem(facade.findGarageById(model.getKey()));
        } else {
            model.setItem(new ParkingGarage());
        }

        if(!has(model.getItem().getAddress())) {
            model.getItem().setAddress(new Address());
        }
    }

    public void remove() {
        if (facade.remove(model.getItem())) {
            addDetailMessage("Parking Garage " + model.getItem().getName()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("/admin/parkingGarage-list.xhtml");
        }
    }

    public void save() {
        try {
            StringBuilder msg = new StringBuilder("Parking Garage " + model.getItem().getName());

            if (model.getKey() == null) {
                facade.create(model.getItem());
                msg.append(" created successfully");
            } else {
                facade.edit(model.getItem());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
    }

    public void clear() {
        model.setItem(new ParkingGarage());
        model.setKey(null);
    }

    public boolean isNew() {
        return model.getItem() == null || model.getItem().getKey() == null;
    }

    public void resetAccessKey() {
        facade.setAccessKey(model.getItem());
        addDetailMessage("Reset AccessKey");
    }

    public GarageFormModel getModel() {
        return model;
    }
}
