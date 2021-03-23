package com.neo.parkguidance.user.web.pages.parkdata;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.primefaces.model.charts.line.LineChartModel;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class GarageDataModel implements Serializable {

    private boolean isInitialized = false;

    private Integer id;
    private ParkingGarage parkingGarage;
    private LineChartModel cartesianLinerModel;

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
