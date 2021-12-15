package com.neo.parkguidance.web.admin.pages.parkdata;

import com.neo.parkguidance.parkdata.impl.data.ParkDataObject;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;
import java.util.List;

/**
 * The screen model for the ParkData screen
 */
@ViewScoped
public class ParkDataModel implements Serializable {

    private boolean initialized = false;

    private List<ParkDataObject> parkDataObject;

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public List<ParkDataObject> getParkDataObject() {
        return parkDataObject;
    }

    public void setParkDataObject(List<ParkDataObject> parkDataObject) {
        this.parkDataObject = parkDataObject;
    }
}
