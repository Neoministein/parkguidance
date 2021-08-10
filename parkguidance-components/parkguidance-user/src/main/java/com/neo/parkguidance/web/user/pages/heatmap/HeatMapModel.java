package com.neo.parkguidance.web.user.pages.heatmap;

import org.json.JSONObject;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;
import java.util.List;

/**
 * The screen model for the data HeatMa
 */
@ViewScoped
public class HeatMapModel implements Serializable {

    private boolean initialized;

    private boolean colorBlind = false;
    private Integer timeOfDay = 12;
    private String gMapUrl;
    private List<JSONObject> heatMapGradiantNormal;
    private List<JSONObject> heatMapGradiantColorBlind;

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isColorBlind() {
        return colorBlind;
    }

    public void setColorBlind(boolean colorBlind) {
        this.colorBlind = colorBlind;
    }

    public Integer getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(Integer timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getgMapUrl() {
        return gMapUrl;
    }

    public void setgMapUrl(String gMapUrl) {
        this.gMapUrl = gMapUrl;
    }

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapGradiantNormal;
    }

    public void setHeatMapGradiantNormal(List<JSONObject> heatMapGradiantNormal) {
        this.heatMapGradiantNormal = heatMapGradiantNormal;
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapGradiantColorBlind;
    }

    public void setHeatMapGradiantColorBlind(List<JSONObject> heatMapGradiantColorBlind) {
        this.heatMapGradiantColorBlind = heatMapGradiantColorBlind;
    }
}
