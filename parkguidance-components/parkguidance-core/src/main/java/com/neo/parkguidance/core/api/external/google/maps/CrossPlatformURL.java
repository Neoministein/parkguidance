package com.neo.parkguidance.core.api.external.google.maps;

import com.neo.parkguidance.core.api.external.google.GoogleApi;
import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named(value = CrossPlatformURL.BEAN_NAME)
public class CrossPlatformURL {
    public static final String BEAN_NAME = "crossPlatformURL";

    public static final String API_VERSION = "?api=1";

    public static String search(ParkingGarage parkingGarage) {
        return "https://www.google.com/maps/search/"+ API_VERSION  +
                GoogleApi.QUERY + getLocation(parkingGarage);
    }

    public static String direction(ParkingGarage parkingGarage) {
        return "https://www.google.com/maps/dir/"+ API_VERSION  +
                GoogleApi.DIRECTION + getLocation(parkingGarage);
    }

    protected static String getLocation(ParkingGarage parkingGarage){
        return (parkingGarage.getAddress().toString() + " " +parkingGarage.toString()).replace(" ","+");

    }

}
