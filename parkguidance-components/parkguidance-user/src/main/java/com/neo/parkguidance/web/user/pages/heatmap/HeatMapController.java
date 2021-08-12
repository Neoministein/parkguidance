package com.neo.parkguidance.web.user.pages.heatmap;

import org.json.JSONObject;

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

    @PostConstruct
    public void init() {

        if (!model.isInitialized()) {
            model.setgMapUrl(facade.generateMapUrl());
            model.setHeatMapGradiantNormal(facade.getHeatMapGradiantNormal());
            model.setHeatMapGradiantColorBlind(facade.getHeatMapGradiantColorBlind());
            model.setInitialized(true);
            JSONObject data;

            if (model.isColorBlind()) {
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
        sendUpdate();
    }

    public void sendUpdate() {
        JSONObject data;
        if (model.isColorBlind()) {
            data = model.getHeatMapGradiantColorBlind().get(model.getTimeOfDay());
        } else {
            data = model.getHeatMapGradiantNormal().get(model.getTimeOfDay());
        }
        facade.updateLocalMap(data);
    }

    public HeatMapModel getModel() {
        return model;
    }
}
