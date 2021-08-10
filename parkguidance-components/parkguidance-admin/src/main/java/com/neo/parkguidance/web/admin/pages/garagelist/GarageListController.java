package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyController;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * The controller for the GarageList screen
 */
@RequestScoped
@Named(value = GarageListController.BEAN_NAME)
public class GarageListController extends AbstractLazyController<ParkingGarage> {

    public static final String BEAN_NAME = "garageList";

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }
}
