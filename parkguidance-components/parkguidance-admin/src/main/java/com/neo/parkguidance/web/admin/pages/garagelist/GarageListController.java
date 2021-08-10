package com.neo.parkguidance.web.admin.pages.garagelist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

/**
 * The controller for the GarageList screen
 */
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
        if(!model.isInstantiated()) {
            clearFilter();
            model.setData(facade.initDataModel(model.getFilter()));
            model.setInstantiated(true);
        }
    }

    public void clearFilter() {
        model.setFilter(facade.newFilter());
    }

    public void delete() {
        int num = facade.delete(model.getSelected());
        if(num != 0) {
            addDetailMessage(num + " ParkingGarage deleted successfully!");
            model.getSelected().clear();
        }
    }

    public GarageListModel getModel() {
        return model;
    }
}
