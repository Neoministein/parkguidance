package com.neo.parkguidance.framework.impl.geomap;

import com.neo.parkguidance.framework.api.geomap.GeoCodingService;
import com.neo.parkguidance.framework.entity.Address;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class GeoCodingServiceImpl implements GeoCodingService {

    @Override
    public void findCoordinates(Address address) {

    }

    @Override
    public void findAddress(Address address) {

    }
}
