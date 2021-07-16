package com.neo.parkguidance.web.user.pages.data;

import org.primefaces.model.charts.line.LineChartDataSet;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DataChartModel implements Serializable {

    private boolean isInitialized = false;

    private Date lastUpdate = new Date(0);

    private List<String> labels;
    private Map<String, LineChartDataSet> dataSets = new HashMap<>();

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Map<String, LineChartDataSet> getDataSets() {
        return dataSets;
    }

    public void setDataSets(Map<String, LineChartDataSet> dataSets) {
        this.dataSets = dataSets;
    }
}
