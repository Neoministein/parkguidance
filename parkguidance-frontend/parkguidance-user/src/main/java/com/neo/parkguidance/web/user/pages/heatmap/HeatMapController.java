package com.neo.parkguidance.web.user.pages.heatmap;

import com.neo.parkguidance.framework.api.geomap.component.HeatmapComponentLogic;
import com.neo.parkguidance.web.user.impl.UserConfig;
import org.json.JSONObject;
import org.primefaces.event.SlideEndEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *  The controller for the HeatMap screen
 */
@RequestScoped
@Named(HeatMapController.BEAN_NAME)
public class HeatMapController {

    public static final String BEAN_NAME = "heatmap";

    @Inject
    HeatMapModel model;

    @Inject
    HeatMapFacade facade;

    @Inject
    UserConfig config;

    @PostConstruct
    public void init() {
        if (!model.isInitialized()) {
            model.setHeatMapGradiantNormal(facade.getHeatMapGradiantNormal());
            model.setHeatMapGradiantColorBlind(facade.getHeatMapGradiantColorBlind());
            model.setInitialized(true);
            JSONObject data;

            if (config.isColorBlind()) {
                data = model.getHeatMapGradiantColorBlind().get(model.getTimeOfDay());
            } else {
                data = model.getHeatMapGradiantNormal().get(model.getTimeOfDay());
            }
            facade.initLocalMap(data);
        }
    }

    public void updateGMap(ValueChangeEvent event) {
        if (event.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            event.queue();
            return;
        }
        sendUpdate(model.getTimeOfDay());
    }
    public void onSlideEnd(SlideEndEvent event) {
        int value = (int) event.getValue();
        sendUpdate(value);
    }

    public void sendUpdate(int timeOfDay) {
        JSONObject data;
        if (config.isColorBlind()) {
            data = model.getHeatMapGradiantColorBlind().get(timeOfDay);
        } else {
            data = model.getHeatMapGradiantNormal().get(timeOfDay);
        }
        facade.updateLocalMap(data);
    }

    public HeatmapComponentLogic getHeatMapLogic() {
        return facade.getHeatmapComponentLogic();
    }

    public HeatMapModel getModel() {
        return model;
    }
}
