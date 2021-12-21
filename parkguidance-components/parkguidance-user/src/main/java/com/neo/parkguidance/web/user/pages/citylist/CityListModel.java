package com.neo.parkguidance.web.user.pages.citylist;

import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.*;

/**
 * The screen model for the ParkList screen
 */
@ApplicationScoped
public class CityListModel implements Serializable {

    private Map<String, List<ParkingGarage>> garageData = new HashMap<>();

    private Date lastUpdate = new Date(0);

    public Map<String, List<ParkingGarage>> getGarageData() {
        return garageData;
    }

    public void setGarageData(Map<String, List<ParkingGarage>> garageData) {
        this.garageData = garageData;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
