package com.neo.parkguidance.web.user.pages.parkdata;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.DataSheetEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingDataEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class GarageDataFacade {

    private static final int HALF_HOURS_IN_DAY = 48;

    @Inject
    private DataSheetEntityService dataSheetService;

    @Inject
    private ParkingGarageEntityService parkingGarageManager;

    @Inject
    private ParkingDataEntityService parkingDataService;

    public LineChartDataSet[] loadDataSet() {
        int sizeOfDataSet = parkingGarageManager.findHighestId().getId().intValue();

        LineChartDataSet[] dataSets = new LineChartDataSet[sizeOfDataSet+1];

        for(ParkingGarage parkingGarage: parkingGarageManager.findAll()) {
            List<Object> averageOccupied = new ArrayList<>();
            for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
                int sum = 0;

                List<DataSheet> dataOfHour = dataSheetService.findOfHour(i,parkingGarage);
                if(!dataOfHour.isEmpty()) {
                    for (DataSheet dataSheet : dataOfHour) {
                        sum += dataSheet.getOccupied();
                    }
                    averageOccupied.add(sum / dataOfHour.size());
                } else {
                    averageOccupied.add(0);
                }

            }
            LineChartDataSet dataSet = new LineChartDataSet();
            dataSet.setData(averageOccupied);
            dataSet.setLabel("Normal");
            dataSet.setYaxisID("left-y-axis");

            dataSets[parkingGarage.getId().intValue()] = dataSet;
        }

        return dataSets;
    }

    public void createCartesianLinerModel(GarageDataModel model, GarageDataChartModel chartModel) {
        LineChartModel cartesianLinerModel = new LineChartModel();
        ChartData data = new ChartData();

        data.addChartDataSet(chartModel.getDataSets()[model.getId()]);

        data.setLabels(chartModel.getLabels());
        cartesianLinerModel.setData(data);

        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");

        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Cartesian Linear Chart");
        options.setTitle(title);

        cartesianLinerModel.setOptions(options);

        model.setCartesianLinerModel(cartesianLinerModel);
    }

    public List<String> createChartLabel() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
            labels.add(i/2 + ":" + (( i % 2 == 0 ) ? "00" : "30"));
        }
        return labels;
    }

    public void setParkingGarage(GarageDataModel model) {
       model.setParkingGarage(parkingGarageManager.find(Long.valueOf(model.getId())));
       model.setOccupied(parkingDataService.getCurrentCapacity(model.getParkingGarage()));
    }
}
