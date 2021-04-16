package com.neo.parkguidance.web.user.pages.geolocation;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class GeoLocationModel implements Serializable {

    private Double latitude;
    private Double longitude;
    private Double accuracyCurrentPosition;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAccuracyCurrentPosition() {
        return accuracyCurrentPosition;
    }

    public void setAccuracyCurrentPosition(Double accuracyCurrentPosition) {
        this.accuracyCurrentPosition = accuracyCurrentPosition;
    }
}
