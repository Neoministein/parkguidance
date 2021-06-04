package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.core.entity.ParkingGarage;

public class CrossPlatformURL {

    public static final String API_VERSION = "?api=1";

    private CrossPlatformURL() {}

    public static String search(ParkingGarage parkingGarage) {
        return "https://www.google.com/maps/search/"+ API_VERSION  +
                GoogleConstants.QUERY + getLocation(parkingGarage);
    }

    public static String direction(ParkingGarage parkingGarage) {
        return "https://www.google.com/maps/dir/"+ API_VERSION  +
                GoogleConstants.DIRECTION + getLocation(parkingGarage);
    }

    protected static String getLocation(ParkingGarage parkingGarage){
        return (parkingGarage.getAddress().toString() + " " +parkingGarage.toString()).replace(" ","+");

    }

}
