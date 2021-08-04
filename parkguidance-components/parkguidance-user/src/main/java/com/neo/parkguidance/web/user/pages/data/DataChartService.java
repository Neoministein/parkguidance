package com.neo.parkguidance.web.user.pages.data;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.model.charts.line.LineChartDataSet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates {@link LineChartDataSet} for the data screen
 */
@Stateless
public class DataChartService {

    private static final Logger LOGGER = LogManager.getLogger(DataChartService.class);

    private static final int HALF_HOURS_IN_DAY = 48;
    public static final String ELASTIC_UNSORTED_INDEX = "/raw-parking-data";

    @Inject
    AbstractEntityDao<ParkingGarage> dao;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public Map<String, LineChartDataSet> loadDataSet() {
        LOGGER.info("Loading Chart Dataset");
        Map<String, LineChartDataSet> dataSetMap = new HashMap<>();

        for(ParkingGarage parkingGarage: dao.findAll()) {
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


    public List<String> createChartLabel() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
            labels.add(i/2 + ":" + (( i % 2 == 0 ) ? "00" : "30"));
        }
        return labels;
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
            LOGGER.warn("Unable to get Average Occupation at halfhour [{}] in [{}]", halfHour, key);
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
