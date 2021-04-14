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

    public void currentPosition() {
        PrimeFaces.current().executeScript("getLocation();");
    }

    public void geoLocationListener(){
        Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        model.setLatitude(Float.valueOf(parameterMap.get("currentLatitude")));
        model.setLongitude(Float.valueOf(parameterMap.get("currentLongitude")));
        model.setAccuracyCurrentPosition(Float.valueOf(parameterMap.get("accuracyCurrentPosition")));

        PrimeFaces.current().ajax().update("coords");
    }


    public GeoLocationModel getModel() {
        return model;
    }
}
