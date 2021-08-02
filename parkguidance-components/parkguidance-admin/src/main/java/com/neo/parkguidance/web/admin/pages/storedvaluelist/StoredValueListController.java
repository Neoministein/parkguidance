package com.neo.parkguidance.web.admin.pages.storedvaluelist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

/**
 * The controller for the StoredValueList screen
 */
@RequestScoped
@Named(StoredValueListController.BEAN_NAME)
public class StoredValueListController {

    public static final String BEAN_NAME = "storedValueList";

    @Inject
    StoredValueListFacade facade;

    @Inject
    StoredValueListModel model;

    @PostConstruct
    public void initDataModel() {
        if(!model.isInitialized()) {
            clearFilter();
            model.setData(facade.initDataModel(model.getFilter()));
            model.setInitialized(true);
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

    public StoredValueListModel getModel() {
        return model;
    }
}
