package com.neo.parkguidance.web.api.component;

import org.json.JSONObject;

public interface HeatmapComponentLogic {

    String generateHTML();

    void initMap(JSONObject data);

    void updateMap(JSONObject data);
}
