package com.neo.parkguidance.web.admin.pages.sheetlist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = SheetListController.BEAN_NAME)
public class SheetListController {

    public static final String BEAN_NAME = "dataSheetList";

    @Inject
    private SheetListFacade facade;

    @Inject
    private SheetListModel model;

    @PostConstruct
    public void init() {
        if(!model.isInstantiated()) {
            facade.initDataModel(model);
            model.setSorterOffline(facade.sorterOffline());
            model.setInstantiated(true);
        }
    }

    public void clear() {
        facade.clearFilter(model);
    }

    public void delete() {
        facade.delete(model);
    }

    public void sortData() {
        facade.sortData();
    }

    public SheetListModel getModel() {
        return model;
    }
}
