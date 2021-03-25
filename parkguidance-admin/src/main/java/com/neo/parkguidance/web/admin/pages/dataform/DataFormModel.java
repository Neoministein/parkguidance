package com.neo.parkguidance.web.admin.pages.dataform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;
import java.util.List;

@ViewScoped
public class DataFormModel implements Serializable {

    private Integer id;
    private ParkingData item;
    private List<ParkingGarage> parkingGarages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ParkingData getItem() {
        return item;
    }

    public void setItem(ParkingData item) {
        this.item = item;
    }

    public List<ParkingGarage> getParkingGarages() {
        return parkingGarages;
    }

    public void setParkingGarages(List<ParkingGarage> parkingGarages) {
        this.parkingGarages = parkingGarages;
    }
}
