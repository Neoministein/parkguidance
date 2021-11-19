package com.neo.parkguidance.web.user.pages.heatmap;

import com.neo.parkguidance.core.api.geomap.component.HeatmapComponentLogic;
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

    @Inject
    HeatMapDataService heatMapDataService;

    @Inject HeatmapComponentLogic heatmapComponentLogic;

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapDataService.getHeatMapGradiantNormal();
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapDataService.getHeatMapGradiantColorBlind();
    }

    public void initLocalMap(JSONObject data) {
        callLocalJavascript(heatmapComponentLogic.initMap(data));
    }

    public void updateLocalMap(JSONObject data) {
        callLocalJavascript(heatmapComponentLogic.updateMap(data));
    }

    public HeatmapComponentLogic getHeatmapComponentLogic() {
        return heatmapComponentLogic;
    }

    private void callLocalJavascript(String command) {
        PrimeFaces.current().executeScript(command);
    }
}
