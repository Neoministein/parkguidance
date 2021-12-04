package com.neo.parkguidance.framework.impl.wrapper.entity;

import org.json.JSONObject;

public class AbstractJSONWrapper {

    private Object opt(JSONObject jsonObject, String key, Class<?> clazz) {
        Object o = jsonObject.opt(key);
        if (clazz.isInstance(o)) {
            return o;
        }
        return null;
    }

    public Object opt(JSONObject jsonObject, String key) {
        return opt(jsonObject, key, Object.class);
    }

    public Integer optInteger(JSONObject jsonObject, String key) {
        return (Integer) opt(jsonObject, key, Integer.class);
    }

    public String optString(JSONObject jsonObject, String key) {
        return (String) opt(jsonObject, key, String.class);
    }

    public Double optDouble(JSONObject jsonObject, String key) {
        return (Double) opt(jsonObject, key, Double.class);
    }

    public Long optLong(JSONObject jsonObject, String key) {
        return (Long) opt(jsonObject, key, Long.class);
    }

    public Boolean optBoolean(JSONObject jsonObject, String key) {
        return (Boolean) opt(jsonObject, key, Boolean.class);
    }
}
