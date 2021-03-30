package com.neo.parkguidance.web.admin.pages.datalist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

@RequestScoped
@Named(value = ParkingDataListController.BEAN_NAME)
public class ParkingDataListController {

    public static final String BEAN_NAME = "parkingDataList";

    @Inject
    ParkingDataListFacade facade;

    @Inject
    ParkingDataListModel model;

    @PostConstruct
    public void init() {
        if(!model.isInstantiated()) {
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
            addDetailMessage(numCars + " ParkingGarage deleted successfully!");
            model.getSelected().clear();
        }
    }

    public ParkingDataListModel getModel() {
        return model;
    }
}
