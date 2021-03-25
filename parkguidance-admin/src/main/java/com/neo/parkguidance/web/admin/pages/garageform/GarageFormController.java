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
    private GarageFormModel model;

    @Inject
    private GarageFormFacade facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }
        if (has(model.getId())) {
            model.setParkingGarage(facade.findGarageById(model.getId()));
        } else {
            model.setParkingGarage(new ParkingGarage());
        }

        if(!has(model.getParkingGarage().getAddress())) {
            model.getParkingGarage().setAddress(new Address());
        }
    }

    public GarageFormModel getModel() {
        return model;
    }

    public void remove() throws IOException {
        if (facade.remove(model.getParkingGarage())) {
            addDetailMessage("Parking Garage " + model.getParkingGarage().getName()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("parkingGarage-list.xhtml");
        }
    }

    public void save() {
        StringBuilder msg = new StringBuilder("Parking Garage " + model.getParkingGarage().getName());

        if (model.getParkingGarage().getId() == null) {
            facade.create(model.getParkingGarage());
            msg.append(" created successfully");
        } else {
            facade.edit(model.getParkingGarage());
            msg.append(" updated successfully");
        }

        addDetailMessage(msg.toString());
    }

    public void clear() {
        model.setParkingGarage(new ParkingGarage());
        model.setId(null);
    }

    public boolean isNew() {
        return model.getParkingGarage() == null || model.getParkingGarage().getId() == null;
    }
}
