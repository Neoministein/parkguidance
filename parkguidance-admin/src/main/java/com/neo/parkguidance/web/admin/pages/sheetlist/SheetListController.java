package com.neo.parkguidance.web.admin.pages.sheetlist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

@RequestScoped
@Named(value = SheetListController.BEAN_NAME)
public class SheetListController {

    public static final String BEAN_NAME = "dataSheetList";

    @Inject
    SheetListFacade facade;

    @Inject
    SheetListModel model;

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

    public SheetListModel getModel() {
        return model;
    }
}
