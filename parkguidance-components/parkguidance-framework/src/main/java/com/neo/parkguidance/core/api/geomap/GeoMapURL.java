package com.neo.parkguidance.core.api.geomap;

import com.neo.parkguidance.core.entity.ParkingGarage;

/**
 * This interface defines the functionality to generate a URL to a geo services based on the ParkingGarage
 */
public interface GeoMapURL {

    /**
     * Returns an URL which will land the user on the same page as a search does
     *
     * @param parkingGarage the desired ParkingGarage
     * @return the URL to the external services
     */
    String search(ParkingGarage parkingGarage);

    /**
     * Returns an URL which will land the user on a directions page to the ParkingGarage
     *
     * @param parkingGarage the desired ParkingGarage
     * @return the URL to the external service
     */
    String direction(ParkingGarage parkingGarage);
}
