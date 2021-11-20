package com.neo.parkguidance.framework.api.geomap;

import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.impl.geomap.DistanceDataObject;

import java.util.List;

/**
 * This interface defines the functionality of the Distance Matrix Service and needs to be implemented by possible
 * geo data services like Google Could Platform or Open Routs.
 * The interface implementation can then be selected with cdi alternatives.
 */
public interface DistanceMatrixService {

    /**
     * Returns the parking garages and their distances to the latitude and longitude to a desired position
     * with additional metadata. If something goes wrong when calling an external service a
     * {@link com.neo.parkguidance.framework.impl.geomap.GeoMapException} will be thrown.
     *
     * @param parkingGarageList the parking garage to calculate the distances from
     * @param latitude of the desired location
     * @param longitude of the desired location
     *
     * @return a list with additional metadata to the distance
     */
    List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, double latitude, double longitude);

    /**
     * Returns the parking garages and their distances to the desired address with additional metadata.
     *  If something goes wrong when calling an external service a
     *  {@link com.neo.parkguidance.framework.impl.geomap.GeoMapException} will be thrown.
     *
     * @param parkingGarageList the parking garage to calculate the distances from
     * @param address of the desired location
     *
     * @return a list with additional metadata to the distance
     */
    List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, Address address);
}
