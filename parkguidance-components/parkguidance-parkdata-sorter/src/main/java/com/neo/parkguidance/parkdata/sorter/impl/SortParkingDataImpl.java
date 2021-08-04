package com.neo.parkguidance.parkdata.sorter.impl;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.event.ParkDataChangeEvent;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObserverException;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class handles the sorting of the ParkingData in the ElasticSearch using {@link Event<ParkDataChangeEvent>}
 */
@Stateless
public class SortParkingDataImpl {

    public static final String ELASTIC_UNSORTED_INDEX = "raw-parking-data";
    public static final String ELASTIC_SORTED_INDEX = "sorted-parking-data";

    public static final String ELASTIC_QUERY = "query";
    public static final String ELASTIC_OCCUPIED = "occupied";
    public static final String ELASTIC_TIMESTAMP = "timestamp";

    public static final int MILLISECONDS_IN_HOUR = 3600000;
    public static final int MILLISECONDS_IN_HALF_AN_HOUR = MILLISECONDS_IN_HOUR / 2;

    private static final Logger LOGGER = LogManager.getLogger(SortParkingDataImpl.class);

    @Inject
    Event<ParkDataChangeEvent> changeEvent;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public void eventListener(@Observes ParkDataChangeEvent changeEvent) {
        if (ParkDataChangeEvent.SORT_REQUEST.equals(changeEvent.getStatus())) {
            sortParkingData();
        }
    }

    /**
     * Sorts all ParkingData in ElasticSearch
     */
    public synchronized void sortParkingData() {
        LOGGER.info("Starting to sort ParkingData in ElasticSearch");
        List<ParkingGarage> allParkingGarages = parkingGarageManager.findAll();

        for (ParkingGarage parkingGarage: allParkingGarages) {
            LOGGER.debug("Sorting Data of [{}]", parkingGarage.getKey());
            long[] bounds = getBounds(parkingGarage.getKey());

            if (bounds.length == 0) {
                LOGGER.debug("No new ParkingData found for");
                continue;
            }

            long startDate = bounds[0];
            long endDate = bounds[1];

            long interval = (endDate - startDate) / MILLISECONDS_IN_HALF_AN_HOUR;
            int numberOfIterations = Math.toIntExact(interval);

            LOGGER.debug("Sorting bounds StartTime [{}] EndTime [{}] Interval [{}]", startDate, endDate, numberOfIterations);
            JSONObject[] docToStore = new JSONObject[numberOfIterations+1];
            for(int i = 0; i <= numberOfIterations; i++) {
                long entryStart = startDate + MILLISECONDS_IN_HALF_AN_HOUR * i;
                long entryEnd = startDate + MILLISECONDS_IN_HALF_AN_HOUR * (i + 1);

                JSONObject doc = generateInitialDoc(entryStart);
                doc.put("garage", parkingGarage.getKey());

                Integer occupied = getOccupiedBetweenTimestamp(parkingGarage.getKey(), entryStart, entryEnd);
                if (occupied == null) {
                    occupied = docToStore[i-1].getInt(ELASTIC_OCCUPIED);
                }
                LOGGER.debug("Interval [{}] Occupied [{}]", i, occupied);


                doc.put(ELASTIC_OCCUPIED,occupied);
                docToStore[i] = doc;

                elasticSearchProvider.save(ELASTIC_SORTED_INDEX, doc.toString());
            }
            updateAsSorted(parkingGarage.getKey(), startDate, endDate);
            saveSorted(docToStore);
        }
        LOGGER.info("Finished sorting ParkingData");
        fireEvent(new ParkDataChangeEvent(ParkDataChangeEvent.SORTED_RESPONSE));
    }

    private void saveSorted(JSONObject[] docs) {
        for (JSONObject doc: docs) {
            elasticSearchProvider.save(ELASTIC_SORTED_INDEX, doc.toString());
        }
    }

    private JSONObject generateInitialDoc(long time) {
        JSONObject doc = new JSONObject();
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date(time));
        doc.put("year",cl.get(Calendar.YEAR));
        doc.put("week",cl.get(Calendar.WEEK_OF_YEAR));
        doc.put("day",cl.get(Calendar.DAY_OF_WEEK));
        doc.put("halfHour", halfHourOfDate(new Date(time)));

        return doc;
    }

    private long[] getBounds(String key) {
        try {
            String result = elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    ELASTIC_UNSORTED_INDEX + "/_search",
                    getBoarderRequestBody(key));
            JSONObject root = new JSONObject(result);

            JSONObject hitsO = root.getJSONObject("hits");
            JSONArray hitsA = hitsO.getJSONArray("hits");

            if (hitsA.isEmpty()) {
                return new long[] {};
            }

            return new long[] {
                    hitsA.getJSONObject(0).getJSONObject("_source").getLong(ELASTIC_TIMESTAMP),
                    hitsA.getJSONObject(hitsA.length()-1).getJSONObject("_source").getLong(ELASTIC_TIMESTAMP)};
        }catch (IOException e) {
            return new long[] {};
        }
    }

    private Integer getOccupiedBetweenTimestamp(String key, long starTime, long endTime) {
        try {
            String result = elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    ELASTIC_UNSORTED_INDEX + "/_search?size=0&filter_path=aggregations",
                    getOccupiedBetweenTimestampRequestBody(key,starTime,endTime));
            JSONObject jsonObject = new JSONObject(result);
            Object o = jsonObject.getJSONObject("aggregations").getJSONObject("avg_occupation").get("value");

            if (o.equals(JSONObject.NULL)) {
                return null;
            }
            return ((Double) o).intValue();
        }catch (IOException e) {
            LOGGER.warn("Unable to get Occupied between timestamp", e);
        }

        return null;
    }

    private void updateAsSorted(String key, long starTime, long endTime) {
        try {
            elasticSearchProvider.sendLowLevelRequest(
                    "POST",
                    ELASTIC_UNSORTED_INDEX + "/_update_by_query?conflicts=proceed",
                    getSaveRequestBody(key,starTime-1,endTime+1));
        } catch (IOException e) {
            LOGGER.warn("Unable to update data to sorted", e);
        }
    }


    public int halfHourOfDate(Date date) {
        int halfHour;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        halfHour = cal.get(Calendar.HOUR_OF_DAY) * 2;

        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int half = cal.get(Calendar.MINUTE);
        if(half >= 30) {
            halfHour++;
        }

        return halfHour;
    }

    private String getOccupiedBetweenTimestampRequestBody(String key, long startTime, long endTime) {
        JSONObject avgOccupation = ElasticSearchLowLevelQuery.averageAggregation(ELASTIC_OCCUPIED);
        JSONObject avg = ElasticSearchLowLevelQuery.combineToJSONObject("avg", avgOccupation);
        JSONObject aggs = ElasticSearchLowLevelQuery.combineToJSONObject("avg_occupation", avg);


        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry(ELASTIC_QUERY, queryMatchGarageSortedAndTimeStampRange(key, startTime, endTime)),
                new ElasticSearchLowLevelQuery.Entry("aggs", aggs)
        );

        return root.toString();
    }

    private String getBoarderRequestBody(String key) {
        JSONArray must = matchGarageAndSorted(key);
        JSONObject bool = ElasticSearchLowLevelQuery.combineToJSONObject("must",must);
        JSONObject query = ElasticSearchLowLevelQuery.combineToJSONObject("bool",bool);

        JSONObject sort = ElasticSearchLowLevelQuery.combineToJSONObject(ELASTIC_TIMESTAMP,"asc");
        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("sort", sort),
                new ElasticSearchLowLevelQuery.Entry(ELASTIC_QUERY, query)
        );
        return root.toString();
    }

    private String getSaveRequestBody(String key, long startTime, long endTime) {

        JSONObject script = ElasticSearchLowLevelQuery.combineToJSONObject("source","ctx._source.sorted = true;");

        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry(ELASTIC_QUERY, queryMatchGarageSortedAndTimeStampRange(key, startTime, endTime)),
                new ElasticSearchLowLevelQuery.Entry("script", script)
        );

        return root.toString();
    }

    private JSONObject queryMatchGarageSortedAndTimeStampRange(String key, long startTime, long endTime) {
        JSONArray filter = ElasticSearchLowLevelQuery.filter(
                ElasticSearchLowLevelQuery.rangeFilter(ELASTIC_TIMESTAMP, startTime, endTime)
        );

        JSONObject bool = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("must", matchGarageAndSorted(key)),
                new ElasticSearchLowLevelQuery.Entry("filter", filter)
        );

        return ElasticSearchLowLevelQuery.combineToJSONObject(
                "bool", bool
        );
    }

    private JSONArray matchGarageAndSorted(String key) {
        return ElasticSearchLowLevelQuery.combineToArray(
                ElasticSearchLowLevelQuery.match("garage", key),
                ElasticSearchLowLevelQuery.match("sorted",false)
        );
    }

    private void fireEvent(ParkDataChangeEvent event) {
        try {
            changeEvent.fire(event);
        }  catch (ObserverException | IllegalArgumentException ex) {
            LOGGER.warn("An exception occurred while firing event [{}] [{}] {}",
                    event.getType(),
                    event.getStatus(),
                    ex.getMessage());
        }
    }
}