package com.neo.parkguidance.web.admin.pages.parkdata;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the ParkData screen
 */
@RequestScoped
@Named(value = ParkDataController.BEAN_NAME)
public class ParkDataController {

    public static final String BEAN_NAME = "parkdata";

    @Inject
    ParkDataFacade facade;

    @Inject
    ParkDataModel model;

    @PostConstruct
    public void init() {
        if (!model.isInitialized()) {
            model.setParkDataObject(facade.loadParkDataStatistics());
            model.setInitialized(true);
        }
    }

    public void sortParkingData() {
        facade.sortParkingData();
    }

    public void deleteOld() {
        facade.deleteOld();
    }

    public ParkDataModel getModel() {
        return model;
    }
}
