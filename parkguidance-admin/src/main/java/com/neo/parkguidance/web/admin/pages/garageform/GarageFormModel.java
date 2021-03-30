package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@ViewScoped
public class GarageFormModel implements Serializable {

    private Integer id;

    private ParkingGarage parkingGarage;

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
}