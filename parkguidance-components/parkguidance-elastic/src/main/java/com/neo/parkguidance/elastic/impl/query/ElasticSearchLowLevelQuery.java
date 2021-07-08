package com.neo.parkguidance.elastic.impl.query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class ElasticSearchLowLevelQuery {

    private ElasticSearchLowLevelQuery() {}

    public static JSONArray must(JSONObject... matches) {
        JSONArray mustArray = new JSONArray();

        for (JSONObject filter: matches) {
            mustArray.put(filter);
        }

        return mustArray;
    }

    public static JSONObject match(String field, Object value) {
        JSONObject root = new JSONObject();
        JSONObject match = new JSONObject();
        match.put(field,value);
        root.put("match",match);
        return root;
    }

    public static JSONArray filter(JSONObject... filters) {
        JSONArray filterArray = new JSONArray();

        for (JSONObject filter: filters) {
            filterArray.put(filter);
        }

        return filterArray;
    }

    public static JSONObject rangeFilter(String field, Object min, Object max) {
        JSONObject root = new JSONObject();
        JSONObject range = new JSONObject();
        JSONObject jsonKey = new JSONObject();
        jsonKey.put("gte",min);
        jsonKey.put("lt",max);
        range.put(field, jsonKey);
        root.put("range",range);
        return root;
    }

    public static JSONObject averageAggregation(String field) {
        JSONObject avg = new JSONObject();
        avg.put("field",field);
        return avg;
    }

    public static JSONArray combineToArray(JSONObject... matches) {
        JSONArray root = new JSONArray();

        for (JSONObject filter: matches) {
            root.put(filter);
        }

        return root;
    }

    public static JSONObject combineToJSONObject(Map.Entry<String,Object>... aggregation) {
        JSONObject root = new JSONObject();
        for (Map.Entry<String, Object> entry: aggregation) {
            root.put(entry.getKey(), entry.getValue());
        }
        return root;
    }

    public static JSONObject combineToJSONObject(String field, Object aggregation) {
        JSONObject root = new JSONObject();
        root.put(field,aggregation);
        return root;
    }

    public static class Entry implements Map.Entry<String, Object>{

        private final String key;
        private Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            this.value = value;
            return value;
        }
    }
}
