package com.neo.parkguidance.web.user.pages.heatmap;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.web.api.component.heatmap.HeatmapComponentLogic;
import com.neo.parkguidance.web.user.impl.heatmap.HeatMapDataService;
import org.json.JSONObject;

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

    @Inject
    StoredValueService storedValueService;

    @Inject
    HeatmapComponentLogic heatmapComponentLogic;

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapDataService.getHeatMapGradiantNormal();
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapDataService.getHeatMapGradiantColorBlind();
    }

    public void initLocalMap(JSONObject data) {
        heatmapComponentLogic.initMap(data);
    }

    public void updateLocalMap(JSONObject data) {
        heatmapComponentLogic.updateMap(data);
    }

    public HeatmapComponentLogic getHeatmapComponentLogic() {
        return heatmapComponentLogic;
    }
}
