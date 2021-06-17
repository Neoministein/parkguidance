package com.neo.parkguidance.web.user.pages.parkdata;

import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

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

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        if(!chartModel.isInitialized()
                || facade.chartModelOutOfDate(chartModel)
                || chartModel.getDataSets().containsKey(model.getKey())) {

            chartModel.setDataSets(facade.loadDataSet());
            chartModel.setLabels(facade.createChartLabel());
            chartModel.setInitialized(true);
            chartModel.setLastUpdate(new Date());
        }

        if(!model.isInitialized()) {
            model.setParkingGarage(facade.getParkingGarage(model.getKey()));
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
