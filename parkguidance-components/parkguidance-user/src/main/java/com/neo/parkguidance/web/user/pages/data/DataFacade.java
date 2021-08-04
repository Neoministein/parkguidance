package com.neo.parkguidance.web.user.pages.data;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * The controller for the NearAddress screen
 */
@Stateless
public class DataFacade {

    private static final Logger LOGGER = LogManager.getLogger(DataFacade.class);

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    @Inject
    DataChartModel dataChartModel;

    public void redirectToIndex() {
        try {
            LOGGER.info("Redirecting to index due to no ParkingGarage existing");
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("index");
        } catch (Exception e) {
            LOGGER.warn("Unable to redirect back to index screen");
        }

    }

    public LineChartModel createCartesianLinerModel(ParkingGarage parkingGarage) {
        LineChartModel cartesianLinerModel = new LineChartModel();
        ChartData data = new ChartData();

        data.addChartDataSet(dataChartModel.getDataSets().get(parkingGarage.getKey()));

        data.setLabels(dataChartModel.getLabels());
        cartesianLinerModel.setData(data);

        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");

        ticks.setMin(0);
        ticks.setMax(parkingGarage.getSpaces());
        linearAxes.setTicks(ticks);

        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(parkingGarage.getName());
        options.setTitle(title);

        cartesianLinerModel.setOptions(options);

        return cartesianLinerModel;
    }

    public ParkingGarage getParkingGarage(String key) {
       return parkingGarageManager.find(key);
    }
}
