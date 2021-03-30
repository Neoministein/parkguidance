package com.neo.parkguidance.web.admin.pages.dataform;

import com.neo.parkguidance.core.entity.ParkingData;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

@RequestScoped
@Named(value = DataFormController.BEAN_NAME)
public class DataFormController {

    public static final String BEAN_NAME = "dataForm";

    @Inject
    DataFormModel model;

    @Inject
    DataFormFacade facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }
        model.setParkingGarages(facade.getParkingGarageList());
        if (has(model.getId())) {
            model.setItem(facade.findGarageById(model.getId()));
        } else {
            model.setItem(new ParkingData());
        }
    }

    public DataFormModel getModel() {
        return model;
    }

    public void remove() throws IOException {
        if (facade.remove(model.getItem())) {
            addDetailMessage("Parking Data from " + model.getItem().getParkingGarage().getName()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("parkingData-list.xhtml");
        }
    }

    public void save() {
        StringBuilder msg = new StringBuilder("Parking Data for " + model.getItem().getParkingGarage().getName());

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
        model.setItem(new ParkingData());
        model.setId(null);
    }

    public boolean isNew() {
        return model.getItem() == null || model.getItem().getId() == null;
    }
}
