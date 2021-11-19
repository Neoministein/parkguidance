package com.neo.parkguidance.web.impl.component;

import javax.faces.component.UIComponentBase;

public abstract class ParkGuidanceUIComponent extends UIComponentBase {

    public static final String NAME_SPACE = "https://github.com/Neoministein/parkguidance";

    @Override
    public String getFamily() {
        return "com.neo.parkguidance.web.component";
    }
}
