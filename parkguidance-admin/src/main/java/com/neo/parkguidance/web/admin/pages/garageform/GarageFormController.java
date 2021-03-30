package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.IOException;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

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
        if (has(model.getId())) {
            model.setItem(facade.findGarageById(model.getId()));
        } else {
            model.setItem(new ParkingGarage());
        }

        if(!has(model.getItem().getAddress())) {
            model.getItem().setAddress(new Address());
        }
    }

    public GarageFormModel getModel() {
        return model;
    }

    public void remove() throws IOException {
        if (facade.remove(model.getItem())) {
            addDetailMessage("Parking Garage " + model.getItem().getName()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("parkingGarage-list.xhtml");
        }
    }

    public void save() {
        StringBuilder msg = new StringBuilder("Parking Garage " + model.getItem().getName());

        if (model.getItem().getId() == null) {
            facade.create(model.getItem());
            msg.append(" created successfully");
        } else {
            facade.edit(model.getItem());
            msg.append(" updated successfully");
        }

        addDetailMessage(msg.toString());
    }

    public void clear() {
        model.setItem(new ParkingGarage());
        model.setId(null);
    }

    public boolean isNew() {
        return model.getItem() == null || model.getItem().getId() == null;
    }
}
