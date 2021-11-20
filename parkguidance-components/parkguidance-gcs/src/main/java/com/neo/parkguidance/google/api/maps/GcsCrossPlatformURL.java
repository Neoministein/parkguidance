package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.framework.api.geomap.GeoMapURL;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.framework.entity.ParkingGarage;

import javax.ejb.Stateless;

/**
 * This class is used to generate urls to lead to Google Services
 */
@Stateless
public class GcsCrossPlatformURL implements GeoMapURL {

    public static final String API_VERSION = "?api=1";

    private GcsCrossPlatformURL() {}

    public String search(ParkingGarage parkingGarage) {
        return "https://www.google.com/maps/search/"+ API_VERSION  +
                GoogleConstants.QUERY + getLocation(parkingGarage);
    }

    public String direction(ParkingGarage parkingGarage) {
        return "https://www.google.com/maps/dir/"+ API_VERSION  +
                GoogleConstants.DIRECTION + getLocation(parkingGarage);
    }

    protected String getLocation(ParkingGarage parkingGarage){
        return (parkingGarage.getAddress().toString() + " " +parkingGarage.toString()).replace(" ","+");

    }

}
