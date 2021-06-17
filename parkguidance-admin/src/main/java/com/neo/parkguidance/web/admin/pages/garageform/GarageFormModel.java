package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@ViewScoped
public class GarageFormModel implements Serializable {

    private String key;

    private ParkingGarage item;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ParkingGarage getItem() {
        return item;
    }

    public void setItem(ParkingGarage item) {
        this.item = item;
    }
}
