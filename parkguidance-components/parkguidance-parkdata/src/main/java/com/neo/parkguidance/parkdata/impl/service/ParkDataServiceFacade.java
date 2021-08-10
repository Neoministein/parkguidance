package com.neo.parkguidance.parkdata.impl.service;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ParkDataServiceFacade {

    private static final Logger LOGGER = LogManager.getLogger(ParkDataServiceFacade.class);

    private static final int HALF_HOURS_IN_DAY = 48;
    public static final String ELASTIC_SORTED_INDEX = "/sorted-parking-data";

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public Map<String, List<Integer>> requestParkData() {
        Map<String, List<Integer>> parkData = new HashMap<>();
        for (ParkingGarage parkingGarage: parkingGarageDao.findAll()) {
            List<Integer> garageData = new ArrayList<>();
            for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
                garageData.add(getAverageOccupation(parkingGarage.getKey(), i));
            }
            parkData.put(parkingGarage.getKey(), garageData);
        }

        return parkData;
    }

    public List<Integer> getEmptyParkData() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
            data.add(0);
        }
        return data;
    }

    private Integer getAverageOccupation(String key, int halfHour) {
        try {
            String result = elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    ELASTIC_SORTED_INDEX + "/_search?size=0&filter_path=aggregations",
                    getAverageOccupationBody(key,halfHour));
            JSONObject jsonObject = new JSONObject(result);
            Object o = jsonObject.getJSONObject("aggregations").getJSONObject("avg_occupation").get("value");

            if (o.equals(JSONObject.NULL)) {
                return 0;
            }

            return (int) Math.round((Double) o);
        }catch (IOException e) {
            LOGGER.warn("Unable to get Average Occupation at halfhour [{}] in [{}] {}", halfHour, key, e);
        }

        return 0;
    }

    private String getAverageOccupationBody(String key, int halfHour) {
        JSONArray must = ElasticSearchLowLevelQuery.combineToArray(
                ElasticSearchLowLevelQuery.match("garage", key),
                ElasticSearchLowLevelQuery.match("halfHour",halfHour));

        JSONObject bool = ElasticSearchLowLevelQuery.combineToJSONObject("must",must);
        JSONObject query = ElasticSearchLowLevelQuery.combineToJSONObject("bool",bool);

        JSONObject avg = ElasticSearchLowLevelQuery.averageAggregation("occupied");
        JSONObject avgOccupation = ElasticSearchLowLevelQuery.combineToJSONObject("avg",avg);
        JSONObject aggs = ElasticSearchLowLevelQuery.combineToJSONObject("avg_occupation",avgOccupation);


        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("query", query),
                new ElasticSearchLowLevelQuery.Entry("aggs", aggs)
        );

        return root.toString();
    }
}
