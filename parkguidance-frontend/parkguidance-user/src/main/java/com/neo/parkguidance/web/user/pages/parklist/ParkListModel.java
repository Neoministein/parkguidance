package com.neo.parkguidance.web.user.pages.parklist;

import com.neo.parkguidance.framework.entity.ParkingGarage;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The screen model for the ParkList screen
 */
@ApplicationScoped
public class ParkListModel implements Serializable {

    private List<ParkingGarage> parkingGarageList = new ArrayList<>();

    private Date lastUpdate = new Date(0);

    public List<ParkingGarage> getParkingGarageList() {
        return parkingGarageList;
    }

    public void setParkingGarageList(List<ParkingGarage> parkingGarageList) {
        this.parkingGarageList = parkingGarageList;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
