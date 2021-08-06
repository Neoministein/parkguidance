package com.neo.parkguidance.web.user.pages.data;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.charts.line.LineChartModel;

import java.io.Serializable;

/**
 * The screen model for the data screen
 */
@ViewScoped
public class DataModel implements Serializable {

    private boolean isInitialized = false;

    private String key = "";
    private String embeddedGMapsUrl;
    private ParkingGarage parkingGarage;
    private LineChartModel cartesianLinerModel;

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmbeddedGMapsUrl() {
        return embeddedGMapsUrl;
    }

    public void setEmbeddedGMapsUrl(String embeddedGMapsUrl) {
        this.embeddedGMapsUrl = embeddedGMapsUrl;
    }

    public ParkingGarage getParkingGarage() {
        return parkingGarage;
    }

    public void setParkingGarage(ParkingGarage parkingGarage) {
        this.parkingGarage = parkingGarage;
    }

    public LineChartModel getCartesianLinerModel() {
        return cartesianLinerModel;
    }

    public void setCartesianLinerModel(LineChartModel cartesianLinerModel) {
        this.cartesianLinerModel = cartesianLinerModel;
    }
}
