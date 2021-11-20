package com.neo.parkguidance.web.user.impl.heatmap;

import com.neo.parkguidance.framework.impl.event.ParkDataChangeEvent;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
/**
 * This class handles storing and requesting of the Heatmap data
 */
@ApplicationScoped
public class HeatMapDataService implements Serializable {

    private List<JSONObject> heatMapGradiantNormal;
    private List<JSONObject> heatMapGradiantColorBlind;

    @Inject
    HeatMapDataServiceFacade facade;

    @PostConstruct
    public void init() {
        heatMapGradiantNormal = facade.generateHeatMapPoints(true);
        heatMapGradiantColorBlind = facade.generateHeatMapPoints(false);
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapGradiantColorBlind;
    }

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapGradiantNormal;
    }

    public void changeEvent(@Observes ParkDataChangeEvent changeEvent) {
        if (changeEvent.getStatus().equals(ParkDataChangeEvent.SERVICE_RESPONSE)) {
            init();
        }
    }
}
