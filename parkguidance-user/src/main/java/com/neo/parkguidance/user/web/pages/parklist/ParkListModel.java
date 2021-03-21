package com.neo.parkguidance.user.web.pages.parklist;


import com.neo.parkguidance.core.entity.ParkingData;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class ParkListModel implements Serializable {

    private List<ParkingData> parkingData = new ArrayList<>();

    private Date lastUpdate = new Date(0);

    public List<ParkingData> getParkingData() {
        return parkingData;
    }

    public void setParkingData(List<ParkingData> parkingData) {
        this.parkingData = parkingData;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
