package com.neo.parkguidance.framework.api.geomap.component;

import com.neo.parkguidance.framework.entity.Address;

public interface EmbeddedMapComponentLogic {

    String generateHTML(Address center);
}
