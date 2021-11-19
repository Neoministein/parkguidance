package com.neo.parkguidance.google.api.maps.components;

import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.api.geomap.component.HeatmapComponentLogic;
import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.impl.utils.ConfigValueUtils;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GcsHeatMapComponentLogic implements HeatmapComponentLogic {

    @Inject
    ConfigService configService;

    @Override
    public String generateHTML() {
        return    "<script src=\"parkguidance/heatmap/heatmap.js\"></script>\n"
                + "<script type=\"text/javascript\" src=\"" + generateMapUrl() +"\"></script>\n"
                + "<div class=\"card\">\n"
                + "    <div id=\"map\" style=\"width:100%; height:80vh\">\n"
                + "    </div>\n"
                + "</div>";
    }

    @Override
    public String initMap(JSONObject data) {
        return "initMap('" + data.toString().replace("\\\"", "$-$-$") + "');";
    }

    @Override
    public String updateMap(JSONObject data) {
        return  "updateHeatMapPoints('" + data.toString().replace("\\\"", "$-$-$") + "')";
    }

    protected String generateMapUrl() {
        return "https://maps.googleapis.com/maps/api/js?key=" + ConfigValueUtils
                .parseString(configService.getConfigMap("com.neo.parkguidance.gcs").get(ConfigValue.V_GOOGLE_MAPS_API_EXTERNAL)) + "&libraries=visualization";
    }
}
