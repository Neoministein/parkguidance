package com.neo.parkguidance.framework.api.geomap;

import com.neo.parkguidance.framework.entity.Address;

/**
 * This interface defines the functionality of the GeoCoding Service and needs to be implemented by possible
 * geo data services like Google Could Platform or Open Routs.
 * The interface implementation can then be selected with cdi alternatives.
 */
public interface GeoCodingService {

    /**
     * This method retrieves the coordinates from the given address and fills the lat and lng in the provided address
     * with the result. If something goes wrong when calling an external service a
     * {@link com.neo.parkguidance.framework.impl.geomap.GeoMapException} will be thrown.
     *
     * @param address the address object that need coordinates
     */
    void findCoordinates(Address address);

    /**
     * This method retrieves the address from the coordinates and fills the lat and lng in the provided address.
     * If something goes wrong when calling an external service a
     * {@link com.neo.parkguidance.framework.impl.geomap.GeoMapException} will be thrown.
     *
     * @param address the address object with the coordinates with the need of an address
     */
    void findAddress(Address address);
}
