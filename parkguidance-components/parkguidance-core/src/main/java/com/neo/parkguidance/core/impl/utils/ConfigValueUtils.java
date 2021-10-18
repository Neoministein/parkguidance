package com.neo.parkguidance.core.impl.utils;

import com.neo.parkguidance.core.entity.ConfigValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S2259") //Null is checked by parseString
public class ConfigValueUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigValueUtils.class);

    private ConfigValueUtils() {}

    private static void checkNull(ConfigValue configValue) {
        if (configValue == null) {
            LOGGER.debug("ConfigValue object is null");
            throw new IllegalArgumentException("ConfigValue object is null");
        }
    }

    public static String parseString(ConfigValue configValue) {
        checkNull(configValue);
        return configValue.getValue();
    }

    public static String parseString(ConfigValue configValue, String defaultValue) {
        try {
            return parseString(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public static int parseInteger(ConfigValue configValue) {
        try {
            return Integer.parseInt(parseString(configValue));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Integer", configValue.getKey());
            throw new IllegalArgumentException(configValue.getKey() + " is not an Integer");
        }
    }

    public static int parseInteger(ConfigValue configValue, int defaultValue) {
        try {
            return parseInteger(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public static long parseLong(ConfigValue configValue) {
        try {
            return Long.parseLong(parseString(configValue));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Long", configValue.getKey());
            throw new IllegalArgumentException(configValue.getKey() + " is not an Long");
        }
    }

    public static long parseLong(ConfigValue configValue, long defaultValue) {
        try {
            return parseLong(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public static boolean parseBoolean(ConfigValue configValue) {
        String bool = parseString(configValue);

        if (bool.equalsIgnoreCase("true")) {
            return true;
        }
        if (bool.equalsIgnoreCase("false")) {
            return false;
        }
        LOGGER.warn("Wrong type specified [{}] is not a Boolean", configValue.getKey());
        throw new IllegalArgumentException(configValue.getKey() + " is not an Boolean");
    }

    public static boolean parseBoolean(ConfigValue configValue, boolean defaultValue) {
        try {
            return parseBoolean(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public static double parseDouble(ConfigValue configValue) {
        try {
            return Double.parseDouble(parseString(configValue));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Double", configValue.getKey());
            throw new IllegalArgumentException(configValue.getKey() + " is not an Double");
        }
    }

    public static double parseDouble(ConfigValue configValue, double defaultValue) {
        try {
            return parseDouble(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public static JSONObject parseJSONObject(ConfigValue configValue) {
        try {
            return new JSONObject(parseString(configValue));
        }catch (JSONException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an JSONObject", configValue.getKey());
            throw new IllegalArgumentException(configValue.getKey() + " is not an JSONObject");
        }
    }

    public static JSONObject parseJSONObject(ConfigValue configValue, JSONObject defaultValue) {
        try {
            return parseJSONObject(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public static JSONArray parseJSONArray(ConfigValue configValue) {
        try {
            return new JSONArray(parseString(configValue));
        }catch (JSONException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an JSONArray", configValue.getKey());
            throw new IllegalArgumentException(configValue.getKey() + " is not an JSONArray");
        }
    }

    public static JSONArray parseJSONArray(ConfigValue configValue, JSONArray defaultValue) {
        try {
            return parseJSONArray(configValue);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }
}
