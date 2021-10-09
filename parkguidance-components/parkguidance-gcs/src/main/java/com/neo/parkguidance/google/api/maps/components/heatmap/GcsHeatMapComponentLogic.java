package com.neo.parkguidance.google.api.maps.components.heatmap;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.web.api.component.heatmap.HeatmapComponentLogic;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GcsHeatMapComponentLogic implements HeatmapComponentLogic {

    @Inject
    StoredValueService storedValueService;

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
    public void initMap(JSONObject data) {
        callLocalJavascript("initMap('" + data.toString().replace("\\\"", "$-$-$") + "');");
    }

    @Override
    public void updateMap(JSONObject data) {
        callLocalJavascript("updateHeatMapPoints('" + data.toString().replace("\\\"", "$-$-$") + "')");
    }

    private void callLocalJavascript(String command) {
        PrimeFaces.current().executeScript(command);
    }

    protected String generateMapUrl() {
        return "https://maps.googleapis.com/maps/api/js?key=" + storedValueService.getString(StoredValue.V_GOOGLE_MAPS_API_EXTERNAL) + "&libraries=visualization";
    }
}
