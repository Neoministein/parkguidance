package com.neo.parkguidance.web.user.pages.data;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller for the NearAddress screen
 */
@Stateless
public class DataFacade {

    public static final String ELASTIC_UNSORTED_INDEX = "/raw-parking-data";

    private static final int HALF_HOURS_IN_DAY = 48;
    private static final long TIME_BETWEEN_UPDATES = 1800000;

    private static final Logger LOGGER = LogManager.getLogger(DataFacade.class);

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public Map<String, LineChartDataSet> loadDataSet() {
        LOGGER.info("Loading Chart Dataset");
        Map<String, LineChartDataSet> dataSetMap = new HashMap<>();

        for(ParkingGarage parkingGarage: parkingGarageManager.findAll()) {
            LOGGER.info("Loading [{}]", parkingGarage.getKey());
            List<Object> averageOccupied = new ArrayList<>();
            for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {

                Integer occupied = getAverageOccupation(parkingGarage.getKey(),i);
                if (occupied == null) {
                    occupied = 0;
                }
                LOGGER.debug("Occupied [{}]", occupied);
                averageOccupied.add(occupied);
            }
            LineChartDataSet dataSet = new LineChartDataSet();
            dataSet.setData(averageOccupied);
            dataSet.setLabel("Normal");
            dataSet.setYaxisID("left-y-axis");

            dataSetMap.put(parkingGarage.getKey(), dataSet);
        }

        return dataSetMap;
    }

    public void createCartesianLinerModel(DataModel model, DataChartModel chartModel) {
        LineChartModel cartesianLinerModel = new LineChartModel();
        ChartData data = new ChartData();

        data.addChartDataSet(chartModel.getDataSets().get(model.getKey()));

        data.setLabels(chartModel.getLabels());
        cartesianLinerModel.setData(data);

        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");

        ticks.setMin(0);
        ticks.setMax(model.getParkingGarage().getSpaces());
        linearAxes.setTicks(ticks);

        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(model.getParkingGarage().getName());
        options.setTitle(title);

        cartesianLinerModel.setOptions(options);

        model.setCartesianLinerModel(cartesianLinerModel);
    }

    public boolean chartModelOutOfDate(DataChartModel model) {
        return  model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis();
    }

    public List<String> createChartLabel() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
            labels.add(i/2 + ":" + (( i % 2 == 0 ) ? "00" : "30"));
        }
        return labels;
    }

    public ParkingGarage getParkingGarage(String key) {
       return parkingGarageManager.find(key);
    }

    private Integer getAverageOccupation(String key, int halfHour) {
        try {
            String result = elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    ELASTIC_UNSORTED_INDEX + "/_search?size=0&filter_path=aggregations",
                    getAverageOccupationBody(key,halfHour));
            JSONObject jsonObject = new JSONObject(result);
            Object o = jsonObject.getJSONObject("aggregations").getJSONObject("avg_occupation").get("value");

            if (o.equals(JSONObject.NULL)) {
                return null;
            }
            return (Integer) o;
        }catch (IOException e) {

        }

        return null;
    }

    private String getAverageOccupationBody(String key, int halfHour) {
        JSONArray must = ElasticSearchLowLevelQuery.combineToArray(
                ElasticSearchLowLevelQuery.match("garage", key),
                ElasticSearchLowLevelQuery.match("halfHour",halfHour));

        JSONObject bool = ElasticSearchLowLevelQuery.combineToJSONObject("must",must);
        JSONObject query = ElasticSearchLowLevelQuery.combineToJSONObject("bool",bool);

        JSONObject avgOccupation = ElasticSearchLowLevelQuery.averageAggregation("occupied");
        JSONObject aggs = ElasticSearchLowLevelQuery.combineToJSONObject("avg_occupation",avgOccupation);


        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("query", query),
                new ElasticSearchLowLevelQuery.Entry("aggs", aggs)
        );

        return root.toString();
    }
}
