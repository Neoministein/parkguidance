package com.neo.parkguidance.web.admin.pages.parkdata;

import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;
import java.util.List;

/**
 * The screen model for the ParkData screen
 */
@ViewScoped
public class ParkDataModel implements Serializable {

    private boolean initialized = false;

    private boolean sorterOffline;
    private List<ParkDataObject> parkDataObject;

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isSorterOffline() {
        return sorterOffline;
    }

    public void setSorterOffline(boolean sorterOffline) {
        this.sorterOffline = sorterOffline;
    }

    public List<ParkDataObject> getParkDataObject() {
        return parkDataObject;
    }

    public void setParkDataObject(List<ParkDataObject> parkDataObject) {
        this.parkDataObject = parkDataObject;
    }
}
