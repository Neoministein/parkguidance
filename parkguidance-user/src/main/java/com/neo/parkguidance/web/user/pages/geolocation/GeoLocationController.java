package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.core.api.external.google.maps.CrossPlatformURL;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Map;

@RequestScoped
@Named(value = GeoLocationController.BEAN_NAME)
public class GeoLocationController {

    public static final String BEAN_NAME = "geoLocation";

    @Inject
    GeoLocationModel model;

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

    public void findByAddress() {
        model.setDistanceDataObjects(facade.callDistance(model.getAddress()));
        PrimeFaces.current().ajax().update("form");
    }

    public void redirectSearch(ParkingGarage parkingGarage) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(CrossPlatformURL.search(parkingGarage));
    }

    public GeoLocationModel getModel() {
        return model;
    }
}
