package com.neo.parkguidance.core.api.geomap.component;

import com.neo.parkguidance.core.entity.Address;

public interface EmbeddedMapComponentLogic {

    String generateHTML(Address center);
}
