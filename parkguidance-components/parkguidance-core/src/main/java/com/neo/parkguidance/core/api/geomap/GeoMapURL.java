package com.neo.parkguidance.core.api.geomap;

import com.neo.parkguidance.core.entity.ParkingGarage;

public interface GeoMapURL {

    String search(ParkingGarage parkingGarage);

    String direction(ParkingGarage parkingGarage);
}
