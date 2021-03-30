package com.neo.parkguidance.web.user.pages.parklist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = ParkListController.BEAN_NAME)
public class ParkListController {

    public static final String BEAN_NAME = "parkList";

    @Inject
    ParkListModel model;

    @Inject
    ParkListFacade  facade;

    @PostConstruct
    public void init() {
        facade.initDataModel(model);
    }

    public ParkListModel getModel() {
        return model;
    }
}
