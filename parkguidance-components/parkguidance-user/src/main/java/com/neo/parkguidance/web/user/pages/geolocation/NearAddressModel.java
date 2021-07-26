package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.google.api.maps.DistanceDataObject;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * The screen model for the NearAddress screen
 */
@SessionScoped
public class NearAddressModel implements Serializable {

    private boolean initiated = false;

    private Address address;
    private List<DistanceDataObject> distanceDataObjects;

    public boolean isInitiated() {
        return initiated;
    }

    public void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<DistanceDataObject> getDistanceDataObjects() {
        return distanceDataObjects;
    }

    public void setDistanceDataObjects(List<DistanceDataObject> distanceDataObjects) {
        this.distanceDataObjects = distanceDataObjects;
    }
}
