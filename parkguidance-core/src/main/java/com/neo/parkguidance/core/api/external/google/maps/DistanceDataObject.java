package com.neo.parkguidance.core.api.external.google.maps;

import com.neo.parkguidance.core.entity.ParkingGarage;

import java.io.Serializable;

public class DistanceDataObject implements Comparable<DistanceDataObject>, Serializable {

    private int distanceInt;
    private String distanceString;

    private int durationInt;
    private String durationString;

    private ParkingGarage parkingGarage;

    public int getDistanceInt() {
        return distanceInt;
    }

    public void setDistanceInt(int distanceInt) {
        this.distanceInt = distanceInt;
    }

    public String getDistanceString() {
        return distanceString;
    }

    public void setDistanceString(String distanceString) {
        this.distanceString = distanceString;
    }

    public int getDurationInt() {
        return durationInt;
    }

    public void setDurationInt(int durationInt) {
        this.durationInt = durationInt;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public ParkingGarage getParkingGarage() {
        return parkingGarage;
    }

    public void setParkingGarage(ParkingGarage parkingGarage) {
        this.parkingGarage = parkingGarage;
    }

    @Override
    public int compareTo(DistanceDataObject o) {
        return this.getDurationInt() - o.getDurationInt();
    }
}
