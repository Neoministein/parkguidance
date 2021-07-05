package com.neo.parkguidance.parkdata.sorter.impl;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class SortParkingDataImpl {

    public static final String ELASTIC_UNSORTED_INDEX = "/raw-parking-data";
    public static final String ELASTIC_SORTED_INDEX = "/sorted-parking-data";

    public static final int MILLISECONDS_IN_HOUR = 3600000;
    public static final int MILLISECONDS_IN_HALF_AN_HOUR = MILLISECONDS_IN_HOUR / 2;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public void sortParkingData() {
        List<ParkingGarage> allParkingGarages = parkingGarageManager.findAll();

        for (ParkingGarage parkingGarage: allParkingGarages) {
            long[] bounds = getBounds(parkingGarage.getKey());

            if (bounds.length == 0) {
                continue;
            }

            long startDate = bounds[0];
            long endDate = bounds[1];

            long interval = (endDate - startDate) / MILLISECONDS_IN_HALF_AN_HOUR;
            int numberOfIterations = Math.toIntExact(interval);

            JSONObject[] docToStore = new JSONObject[numberOfIterations];
            for(int i = 0; i <= numberOfIterations; i++) {
                long entryStart = startDate + MILLISECONDS_IN_HALF_AN_HOUR * i;
                long entryEnd = startDate + MILLISECONDS_IN_HALF_AN_HOUR * (i + 1);

                JSONObject doc = generateInitialDoc(entryStart);
                doc.put("garage", parkingGarage.getKey());

                Integer occupied = getOccupiedBetweenTimestamp(parkingGarage.getKey(), entryStart, entryEnd);
                if (occupied == null) {
                    occupied = docToStore[i-1].getInt("occupied");
                }


                doc.put("occupied",occupied);
                docToStore[i] = doc;

                elasticSearchProvider.save(ELASTIC_SORTED_INDEX, doc.toString());
            }
            updateAsSorted(parkingGarage.getKey(), startDate, endDate);
            saveSorted(docToStore);
        }
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
                    hitsA.getJSONObject(0).getLong("timestamp"),
                    hitsA.getJSONObject(hitsA.length()-1).getLong("timestamp")};
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

            return (Integer) jsonObject.getJSONObject("aggregations").getJSONObject("avg_occupation").get("value");
        }catch (IOException e) {

        }

        return null;
    }

    private void updateAsSorted(String key, long starTime, long endTime) {
        try {
            elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    ELASTIC_UNSORTED_INDEX + "/_update_by_query?conflicts=proceed",
                    getSaveRequestBody(key,starTime,endTime));
        } catch (IOException e) {

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

    private String getOccupiedBetweenTimestampRequestBody(String key, long starTime, long endTime) {
        return ("{"
                    + "\"query\": {"
                        + "\"bool\": { "
                            + "\"must\": ["
                                + "{ \"match\": { \"garage\": \""+key+"\"}},"
                                + "{ \"match\": { \"sorted\": false }}"
                            + "],"
                            + "\"filter\": ["
                                + "{ \"range\": { \"timestamp\": { \"gte\": " + starTime + ", \"lt\": " + endTime + "}}}\n"
                            + "] "
                        + "}"
                    + "},"
                    + "\"aggs\": {"
                        + "\"avg_occupation\": { \"avg\": { \"field\": \"occupied\" }}"
                    + "}"
                + "}");
    }

    private String getBoarderRequestBody(String key) {
        return "{"
                    + "\"sort\": { \"timestamp\": \"asc\"},"
                    + "\"query\": {"
                        + "\"bool\": {"
                            + "\"must\": ["
                                + "{ \"match\": { \"garage\": \"" + key + "\"}},"
                                + "{ \"match\": { \"sorted\": false }}"
                            + "]"
                        + "}"
                    + "}"
                + "}";
    }

    private String getSaveRequestBody(String key, long startTime, long endTime) {
        return "{"
                    + "\"query\": {"
                        + "\"bool\": {"
                            +"\"must\": ["
                                + "{ \"match\": { \"garage\": \"" + key + "\"}},"
                                + "{ \"match\": { \"sorted\": false }}"
                            + "],"
                            + "\"filter\": ["
                                + "{ \"range\": { \"timestamp\": { \"gte\": " + startTime + ", \"lt\": " + endTime + "}}}"
                            +"]"
                        + "}"
                    + "},"
                    + "\"script\": {"
                        + "\"source\": \"ctx._source.sorted = true;\""
                    + "}"
                + "}";
    }
}