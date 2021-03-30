package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@ViewScoped
public class GarageFormModel implements Serializable {

    private Integer id;

    private ParkingGarage item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ParkingGarage getItem() {
        return item;
    }

    public void setItem(ParkingGarage item) {
        this.item = item;
    }
}
