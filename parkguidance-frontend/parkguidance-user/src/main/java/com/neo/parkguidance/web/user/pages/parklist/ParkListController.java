package com.neo.parkguidance.web.user.pages.parklist;

import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.ColorGenerator;
import com.neo.parkguidance.web.user.impl.UserConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the ParkList screen
 */
@RequestScoped
@Named(value = ParkListController.BEAN_NAME)
public class ParkListController {

    public static final String BEAN_NAME = "parkList";

    @Inject
    ParkListModel model;

    @Inject
    ParkListFacade  facade;

    @Inject
    UserConfig config;

    @PostConstruct
    public void init() {
        facade.initDataModel(model);
    }

    public ParkListModel getModel() {
        return model;
    }

    public String getStyleColorOfOccupied(ParkingGarage parkingGarage) {
        return ColorGenerator.getStyleColor(
                parkingGarage.getSpaces(),
                parkingGarage.getOccupied(),
                config.isColorBlind());
    }
}
