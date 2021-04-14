package com.neo.parkguidance.web.user.pages.geolocation;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class GeoLocationModel implements Serializable {

    private Float latitude;
    private Float longitude;
    private Float accuracyCurrentPosition;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getAccuracyCurrentPosition() {
        return accuracyCurrentPosition;
    }

    public void setAccuracyCurrentPosition(Float accuracyCurrentPosition) {
        this.accuracyCurrentPosition = accuracyCurrentPosition;
    }
}
