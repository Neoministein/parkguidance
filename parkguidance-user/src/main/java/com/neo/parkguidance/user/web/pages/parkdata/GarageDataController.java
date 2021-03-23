package com.neo.parkguidance.user.web.pages.parkdata;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = GarageDataController.BEAN_NAME)
public class GarageDataController {

    public static final String BEAN_NAME = "parkingData";

    @Inject
    private GarageDataModel model;

    @Inject
    private GarageDataChartModel chartModel;

    @Inject
    private GarageDataFacade facade;

    @PostConstruct
    public void init() {
        if(!chartModel.isInitialized()) {
            chartModel.setDataSets(facade.loadDataSet());
            chartModel.setLabels(facade.createChartLabel());
            chartModel.setInitialized(true);
        }

        if(!model.isInitialized()) {
            model.setId(1);
            facade.createCartesianLinerModel(model, chartModel);
            model.setInitialized(true);
        }
    }

    public GarageDataModel getModel() {
        return model;
    }

    public GarageDataChartModel getChartModel() {
        return chartModel;
    }
}
