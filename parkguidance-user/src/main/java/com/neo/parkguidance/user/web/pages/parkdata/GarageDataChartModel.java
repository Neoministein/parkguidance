package com.neo.parkguidance.user.web.pages.parkdata;

import org.primefaces.model.charts.line.LineChartDataSet;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class GarageDataChartModel implements Serializable {

    private boolean isInitialized = false;

    private Date lastUpdate = new Date(0);

    private List<String> labels;
    private LineChartDataSet[] dataSets = new LineChartDataSet[0];

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

    public LineChartDataSet[] getDataSets() {
        return dataSets;
    }

    public void setDataSets(LineChartDataSet[] dataSets) {
        this.dataSets = dataSets;
    }
}
