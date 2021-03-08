package com.neo.parkguidance.web.pages.admin.garagelist;

import com.neo.parkguidance.web.security.UserBean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = GarageController.BEAN_NAME)
public class GarageController {

    public static final String BEAN_NAME = "garageList";

    @Inject
    private GarageModel model;

    @Inject
    private UserBean userBean;

    @Inject
    private GarageFacade facade;

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

    public void findGarageById(Integer id) {
        facade.findGarageById(id,model);
    }

    public void delete() {
        facade.delete(model);
    }

    public GarageModel getModel() {
        return model;
    }
}
