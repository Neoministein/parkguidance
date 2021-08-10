package com.neo.parkguidance.web.user.pages.heatmap;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.StoredValueService;
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

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapDataService.getHeatMapGradiantNormal();
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapDataService.getHeatMapGradiantColorBlind();
    }

    public String generateMapUrl() {
        return "https://maps.googleapis.com/maps/api/js?key=" + storedValueService.getString(StoredValue.V_GOOGLE_MAPS_API_EXTERNAL) + "&libraries=visualization";
    }

}
