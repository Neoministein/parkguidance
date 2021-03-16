package com.neo.parkguidance.admin.web.pages.admin.datalist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = ParkingDataListController.BEAN_NAME)
public class ParkingDataListController {

    public static final String BEAN_NAME = "parkingDataList";

    @Inject
    private ParkingDataListFacade facade;

    @Inject
    private ParkingDataListModel model;

    @PostConstruct public void init() {
        facade.initDataModel(model);
    }

    public void clear() {
        facade.clearFilter(model);
    }

    public void delete() {
        facade.delete(model);
    }

    public ParkingDataListModel getModel() {
        return model;
    }
}