package com.neo.parkguidance.web.user.pages.heatmap;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.StoredValueService;
import com.neo.parkguidance.web.user.impl.heatmap.HeatMapDataService;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * The controller for the NearAddress screen
 */
@Stateless
public class HeatMapFacade {

    @Inject HeatMapDataService heatMapDataService;

    @Inject
    StoredValueService storedValueService;

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapDataService.getHeatMapGradiantNormal();
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapDataService.getHeatMapGradiantColorBlind();
    }

    public void initLocalMap(JSONObject data) {
        callLocalJavascript("initMap('" + data.toString().replace("\\\"", "$-$-$") + "');");
    }

    public void updateLocalMap(JSONObject data) {
        callLocalJavascript("updateHeatMapPoints('" + data.toString().replace("\\\"", "$-$-$") + "')");
    }

    private void callLocalJavascript(String command) {
        PrimeFaces.current().executeScript(command);
    }

    public String generateMapUrl() {
        return "https://maps.googleapis.com/maps/api/js?key=" + storedValueService.getString(StoredValue.V_GOOGLE_MAPS_API_EXTERNAL) + "&libraries=visualization";
    }

}
