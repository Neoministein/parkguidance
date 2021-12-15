package com.neo.parkguidance.parkdata.impl.data;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

@Stateless
public class ParkDataDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkDataDao.class);

    private static final int HALF_HOURS_IN_DAY = 48;
    public static final String ELASTIC_SORTED_INDEX = "/sorted-parking-data";
    public static final String ELASTIC_UNSORTED_INDEX = "/raw-parking-data";

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public ParkDataObject retrieveMetadata(ParkingGarage parkingGarage) {
        String key = parkingGarage.getKey();
        Integer rawSortedCount = getRawCount(key, true);
        Integer rawUnsortedCount = getRawCount(key, false);
        Integer sortedDataCount = getSortedCount(key);
        Date lastUpdate = getLastUpdate(key);

        return new ParkDataObject(key,rawSortedCount,rawUnsortedCount,sortedDataCount,lastUpdate);
    }

    public void deleteRawSortedData() {
        try {
            elasticSearchProvider.sendLowLevelRequest("POST",
                    ELASTIC_UNSORTED_INDEX + "/_delete_by_query",
                    getDeleteOldRequestBody());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

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

    private String getDeleteOldRequestBody() {
        JSONArray must = unsorted();
        JSONObject bool = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("must", must)
        );

        JSONObject query = ElasticSearchLowLevelQuery.combineToJSONObject(
                "bool",bool);

        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("query", query)
        );
        return root.toString();
    }

    private JSONArray unsorted() {
        return ElasticSearchLowLevelQuery.combineToArray(
                ElasticSearchLowLevelQuery.match("sorted",true)
        );
    }

    private Integer getSortedCount(String key) {
        try {
            JSONObject response = new JSONObject(elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    "/sorted-parking-data/_count?q=garage:" + key,
                    ""));

            return response.getInt("count");
        } catch (IOException ex) {
            LOGGER.warn("Unable to retrieve sorted data from elasticsearch on garage [{}]", key);
        }
        return -1;
    }

    private Date getLastUpdate(String key) {
        try {
            String jsonBody = "{\n" + "   \"size\": 1,\n" + "   \"sort\": { \"timestamp\": \"desc\"},\n"
                    + "  \"query\" : {\n" + "    \"bool\": { \n" + "      \"must\": [\n"
                    + "        { \"match\": { \"garage\": \"" + key + "\"}}\n" + "      ]\n" + "    }\n" + "  }\n" + "}";

            JSONObject response = new JSONObject(elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    "raw-parking-data/_search",
                    jsonBody

            ));

            JSONArray hits = response.getJSONObject("hits").getJSONArray("hits");

            if (!hits.isEmpty()) {
                JSONObject hit = hits.getJSONObject(0);

                return new Date(hit.getJSONObject("_source").getLong("timestamp"));
            }
        } catch (Exception ex) {
            LOGGER.warn("Unable to retrieve the last data update elasticsearch on key [{}]", key);
        }
        return null;
    }

    private Integer getRawCount(String key, boolean sorted) {
        try {
            String jsonBody = "{\n" + "  \"query\" : {\n" + "    \"bool\": { \n" + "      \"must\": [\n"
                    + "        { \"match\": { \"garage\": \"" + key + "\"}},\n"
                    + "        { \"match\": { \"sorted\": " + sorted + " }}\n" + "      ]\n" + "    }\n" + "  }}";

            JSONObject response = new JSONObject(elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    "/raw-parking-data/_count",
                    jsonBody
            ));

            return response.getInt("count");
        } catch (Exception ex) {
            LOGGER.warn("Unable to retrieve raw data from elasticsearch on garage [{}]", key);
        }
        return -1;
    }
}
