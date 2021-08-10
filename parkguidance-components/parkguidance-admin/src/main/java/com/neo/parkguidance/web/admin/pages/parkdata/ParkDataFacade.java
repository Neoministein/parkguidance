package com.neo.parkguidance.web.admin.pages.parkdata;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.event.ParkDataChangeEvent;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omnifaces.util.Messages;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObserverException;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The screen facade for the ParkData screen
 */
@Stateless
public class ParkDataFacade {

    public static final String ELASTIC_UNSORTED_INDEX = "/raw-parking-data";

    private static final Logger LOGGER = LogManager.getLogger(ParkDataFacade.class);

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    @Inject
    Event<ParkDataChangeEvent> changeEvent;

    @Inject
    AbstractEntityDao<ParkingGarage> entityDao;

    public void sortParkingData() {
        ParkDataChangeEvent event = new ParkDataChangeEvent(ParkDataChangeEvent.SORT_REQUEST);
        try {
            changeEvent.fire(event);
        } catch (ObserverException | IllegalArgumentException ex) {
            LOGGER.warn("An exception occurred while firing event [{}] [{}] {}",
                    event.getType(),
                    event.getStatus(),
                    ex.getMessage());

            Messages.addError(null, "Something went wrong while sorting " + ex.getMessage());
        }
    }

    public void deleteOld() {
        try {
            elasticSearchProvider.sendLowLevelRequest("POST",
                    ELASTIC_UNSORTED_INDEX + "/_delete_by_query",
                    getDeleteOldRequestBody());
            Utils.addDetailMessage("Raw Parking Data has been sorted");
        } catch (IOException ex) {
            Messages.addError(null, "Something went wrong: " , ex.getMessage());
        }
    }

    public List<ParkDataObject> loadParkDataStatistics() {
        List<ParkDataObject> parkDataObjectList = new ArrayList<>();

        for (ParkingGarage parkingGarage: entityDao.findAll()) {
            String key = parkingGarage.getKey();
            Integer rawSortedCount = getRawCount(key, true);
            Integer rawUnsortedCount = getRawCount(key, false);
            Integer sortedDataCount = getSortedCount(key);
            Date lastUpdate = getLastUpdate(key);

            ParkDataObject parkDataObject = new ParkDataObject(key,rawSortedCount,rawUnsortedCount,sortedDataCount,lastUpdate);
            parkDataObjectList.add(parkDataObject);
        }
        return parkDataObjectList;
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
}
