package com.neo.parkguidance.core.api.geomap.component;

import org.json.JSONObject;

public interface HeatmapComponentLogic {

    String generateHTML();

    String initMap(JSONObject data);

    String updateMap(JSONObject data);
}
