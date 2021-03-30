package com.neo.parkguidance.web.admin.pages.sheetlist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

@RequestScoped
@Named(value = DataSheetListController.BEAN_NAME)
public class DataSheetListController {

    public static final String BEAN_NAME = "dataSheetList";

    @Inject DataSheetListFacade facade;

    @Inject DataSheetListModel model;

    @PostConstruct
    public void init() {
        if(!model.isInstantiated()) {
            facade.initDataModel(model);
            model.setSorterOffline(facade.sorterOffline());
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

    public void sortData() {
        facade.sortData();
    }

    public DataSheetListModel getModel() {
        return model;
    }
}
