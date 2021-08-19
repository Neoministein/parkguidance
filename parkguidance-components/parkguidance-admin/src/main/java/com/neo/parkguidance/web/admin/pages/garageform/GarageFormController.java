package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormController;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

/**
 * The controller for the GarageForm screen
 */
@RequestScoped
@Named(value = GarageFormController.BEAN_NAME)
public class GarageFormController extends AbstractFormController<ParkingGarage> {

    public static final String BEAN_NAME = "garageForm";

    @Inject
    GarageFormModel model;

    @Inject
    GarageFormFacade facade;

    @Override
    public void init() {
        super.init();
        if(Faces.isAjaxRequest()){
            return;
        }

        if(!has(getModel().getEntity().getAddress())) {
            getModel().getEntity().setAddress(new Address());
        }
    }

    @Override
    protected String getRedirectLocation() {
        return "admin/parkingGarage-list";
    }

    public void resetAccessKey() {
        facade.setAccessKey(getModel().getEntity());
        addDetailMessage("Reset AccessKey");
    }

    @Override
    public GarageFormModel getModel() {
        return model;
    }

    @Override
    protected GarageFormFacade getFacade() {
        return facade;
    }
}
