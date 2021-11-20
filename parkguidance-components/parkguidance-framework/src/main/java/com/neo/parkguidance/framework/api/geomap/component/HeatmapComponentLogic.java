package com.neo.parkguidance.framework.api.geomap.component;

import org.json.JSONObject;

public interface HeatmapComponentLogic {

    String generateHTML();

    String initMap(JSONObject data);

    String updateMap(JSONObject data);
}
