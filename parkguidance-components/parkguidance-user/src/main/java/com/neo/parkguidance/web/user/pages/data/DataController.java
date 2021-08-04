package com.neo.parkguidance.web.user.pages.data;

import com.neo.parkguidance.web.utils.Utils;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 * The controller for the Data screen
 */
@RequestScoped
@Named(value = DataController.BEAN_NAME)
public class DataController {

    public static final String BEAN_NAME = "data";

    @Inject
    private DataModel model;

    @Inject
    private DataChartModel chartModel;

    @Inject
    private DataFacade facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        model.setParkingGarage(facade.getParkingGarage(model.getKey()));
        if (model.getParkingGarage() == null) {
            Utils.addDetailMessage("Parking Garage " + model.getKey() + " does not exists");
            facade.redirectToIndex();
            return;
        }

        if(!chartModel.isInitialized()
                || facade.chartModelOutOfDate(chartModel)
                || !chartModel.getDataSets().containsKey(model.getKey())) {

            chartModel.setDataSets(facade.loadDataSet());
            chartModel.setLabels(facade.createChartLabel());
            chartModel.setInitialized(true);
            chartModel.setLastUpdate(new Date());
        }

        if(!model.isInitialized()) {
            facade.createCartesianLinerModel(model, chartModel);
            model.setInitialized(true);
        }
    }

    public DataModel getModel() {
        return model;
    }

    public DataChartModel getChartModel() {
        return chartModel;
    }
}
