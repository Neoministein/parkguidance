package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * The controller for the NearAddress screen
 */
@RequestScoped
@Named(value = NearLocationController.BEAN_NAME)
public class NearLocationController {

    public static final String BEAN_NAME = "nearLocation";

    @Inject
    NearLocationModel model;

    @Inject
    GeoLocationFacade facade;

    @PostConstruct
    public void init() {
        if (!model.isInitiated()) {
            model.setAddress(new Address());
            model.setInitiated(true);
        }
    }

    public void geoLocationListener(){
        Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        model.getAddress().setLatitude(Double.valueOf(parameterMap.get("currentLatitude")));
        model.getAddress().setLongitude(Double.valueOf(parameterMap.get("currentLongitude")));
        model.setAccuracyCurrentPosition(Double.valueOf(parameterMap.get("accuracyCurrentPosition")));

        model.setDistanceDataObjects(facade.callDistance(
                model.getAddress().getLatitude(),
                model.getAddress().getLongitude()));

        PrimeFaces.current().ajax().update("form");
    }

    public void redirectSearch(ParkingGarage parkingGarage) {
        facade.redirectSearch(parkingGarage);
    }

    public NearLocationModel getModel() {
        return model;
    }
}
