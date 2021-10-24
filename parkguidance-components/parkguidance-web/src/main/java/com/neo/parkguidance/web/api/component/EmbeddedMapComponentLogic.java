package com.neo.parkguidance.web.api.component;

import com.neo.parkguidance.core.entity.Address;

public interface EmbeddedMapComponentLogic {

    String generateHTML(Address center);
}
