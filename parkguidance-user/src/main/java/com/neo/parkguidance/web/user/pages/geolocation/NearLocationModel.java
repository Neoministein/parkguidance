package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.google.api.maps.DistanceDataObject;
import com.neo.parkguidance.core.entity.Address;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class NearLocationModel implements Serializable {

    private boolean initiated = false;

    private Double accuracyCurrentPosition;
    private Address address;
    private List<DistanceDataObject> distanceDataObjects;

    public boolean isInitiated() {
        return initiated;
    }

    public void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    public Double getAccuracyCurrentPosition() {
        return accuracyCurrentPosition;
    }

    public void setAccuracyCurrentPosition(Double accuracyCurrentPosition) {
        this.accuracyCurrentPosition = accuracyCurrentPosition;
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
