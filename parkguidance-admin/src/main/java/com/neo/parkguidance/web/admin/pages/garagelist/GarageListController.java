package com.neo.parkguidance.web.admin.pages.garagelist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

@RequestScoped
@Named(value = GarageListController.BEAN_NAME)
public class GarageListController {

    public static final String BEAN_NAME = "garageList";

    @Inject
    GarageListModel model;

    @Inject
    GarageListFacade facade;

    @PostConstruct
    public void initDataModel() {
        boolean init = model.isInstantiated();
        if(!init) {
            facade.initDataModel(model);
            model.setInstantiated(true);
        }
    }

    public void clear() {
        model.setFilter(facade.newFilter());
    }

    public void delete() {
        int numCars = facade.delete(model.getSelected());
        if(numCars != 0) {
            addDetailMessage(numCars + "ParkingGarage deleted successfully!");
            model.getSelected().clear();
        }
    }

    public GarageListModel getModel() {
        return model;
    }
}
