package com.neo.parkguidance.web.pages.admin.garagelist;

import com.neo.parkguidance.web.security.UserBean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = GarageListController.BEAN_NAME)
public class GarageListController {

    public static final String BEAN_NAME = "garageList";

    @Inject
    private GarageListModel model;

    @Inject
    private UserBean userBean;

    @Inject
    private GarageListFacade facade;

    @PostConstruct
    public void initDataModel() {
        boolean init = model.isInstantiated();
        if(!init) {
            facade.initDataModel(model);
            model.setInstantiated(true);
        }
    }

    public void clear() {
        facade.clearFilter(model);
    }

    public void delete() {
        facade.delete(model);
    }

    public GarageListModel getModel() {
        return model;
    }
}
