package com.neo.parkguidance.web.user.pages.geolocation;

import org.primefaces.PrimeFaces;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@RequestScoped
@Named(value = GeoLocationController.BEAN_NAME)
public class GeoLocationController {

    public static final String BEAN_NAME = "geoLocation";

    @Inject
    GeoLocationModel model;

    @Inject
    GeoLocationFacade facade;

    public void currentPosition() {
        PrimeFaces.current().executeScript("getLocation();");
    }

    public void geoLocationListener(){
        Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        model.setLatitude(Double.valueOf(parameterMap.get("currentLatitude")));
        model.setLongitude(Double.valueOf(parameterMap.get("currentLongitude")));
        model.setAccuracyCurrentPosition(Double.valueOf(parameterMap.get("accuracyCurrentPosition")));

        PrimeFaces.current().ajax().update("coords");
        facade.callDistance(model.getLatitude(),model.getLongitude());
    }


    public GeoLocationModel getModel() {
        return model;
    }
}
