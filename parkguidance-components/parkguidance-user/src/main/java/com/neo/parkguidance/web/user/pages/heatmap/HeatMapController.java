package com.neo.parkguidance.web.user.pages.heatmap;

import org.json.JSONObject;
import org.primefaces.PrimeFaces;

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
            JSONObject root;

            if (model.isColorBlind()) {
                root = model.getHeatMapGradiantColorBlind().get(model.getTimeOfDay());
            } else {
                root = model.getHeatMapGradiantNormal().get(model.getTimeOfDay());
            }
            PrimeFaces.current().executeScript("initMap('" + root.toString().replace("\\\"", "$-$-$") + "');");
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
        if (model.isColorBlind()) {
            PrimeFaces.current().executeScript("updateHeatMapPoints('" + model.getHeatMapGradiantColorBlind().get(model.getTimeOfDay()).toString() + "')");
        } else {
            PrimeFaces.current().executeScript("updateHeatMapPoints('" + model.getHeatMapGradiantNormal().get(model.getTimeOfDay()).toString() + "')");
        }
    }

    public HeatMapModel getModel() {
        return model;
    }
}
