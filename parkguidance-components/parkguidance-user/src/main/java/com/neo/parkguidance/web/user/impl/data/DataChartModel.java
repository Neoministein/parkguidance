package com.neo.parkguidance.web.user.impl.data;

import com.neo.parkguidance.core.impl.event.ParkDataChangeEvent;
import org.primefaces.model.charts.line.LineChartDataSet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The screen model for the charts in the data screen
 */
@ApplicationScoped
public class DataChartModel implements Serializable {

    private List<String> labels;
    private Map<String, LineChartDataSet> dataSets = new HashMap<>();

    @Inject
    DataChartService dataChartService;

    @PostConstruct
    public void init() {
        labels = dataChartService.createChartLabel();
        dataSets = dataChartService.loadDataSet();
    }

    public void reloadDataSet(@Observes ParkDataChangeEvent changeEvent) {
        if (ParkDataChangeEvent.SERVICE_RESPONSE.equals(changeEvent.getStatus())) {
            init();
        }
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
