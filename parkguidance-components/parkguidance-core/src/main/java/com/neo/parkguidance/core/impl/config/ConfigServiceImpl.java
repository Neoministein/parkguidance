package com.neo.parkguidance.core.impl.config;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.impl.event.DataBaseEntityChangeEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link ConfigService} implementation
 */
@ApplicationScoped
public class ConfigServiceImpl implements ConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Inject
    EntityDao<ConfigValue> dao;

    private Map<String, ConfigValue> storedValues;

    @PostConstruct
    protected void init() {
        List<ConfigValue> allValues = dao.findAll();


        Map<String, ConfigValue> newMap = new HashMap<>();
        for (ConfigValue storedValue: allValues) {
            newMap.put(storedValue.getKey(), storedValue);
        }

        storedValues = newMap;
    }

    public void changeEvent(@Observes DataBaseEntityChangeEvent<ConfigValue> changeEvent) {
        switch (changeEvent.getStatus()) {
        case DataBaseEntityChangeEvent.CREATE:
        case DataBaseEntityChangeEvent.EDIT:
            storedValues.put(changeEvent.getChangedObject().getKey(), changeEvent.getChangedObject());
            break;
        case DataBaseEntityChangeEvent.REMOVE:
            storedValues.remove(changeEvent.getChangedObject().getKey());
            break;
        default:
            init();
        }
        LOGGER.debug("StoredValueService has been updated");
    }

    public void reload() {
        init();
    }

    public ConfigValue getStoredValue(String key) {
        ConfigValue storedValue = storedValues.get(key);
        if (storedValue == null) {
            LOGGER.warn("Unable to find the stored value {}" , key);
            throw new IllegalArgumentException(getClass().getName() + " has no entry for the key " + key);
        }
        return storedValue;
    }

    public String getString(String key) {
        return getStoredValue(key).getValue();
    }

    public String getString(String key, String defaultValue) {
        try {
            return getString(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public int getInteger(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Integer", key);
            throw new IllegalArgumentException(key + " is not an Integer");
        }
    }

    public int getInteger(String key, int defaultValue) {
        try {
            return getInteger(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Long", key);
            throw new IllegalArgumentException(key + " is not an Long");
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            return getLong(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key) {
        String bool = getString(key);

        if (bool.equalsIgnoreCase("true")) {
            return true;
        }
        if (bool.equalsIgnoreCase("false")) {
            return false;
        }
        LOGGER.warn("Wrong type specified [{}] is not a Boolean", key);
        throw new IllegalArgumentException(key + " is not an Boolean");
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            return getBoolean(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Double", key);
            throw new IllegalArgumentException(key + " is not an Double");
        }
    }

    public double getDouble(String key, double defaultValue) {
        try {
            return getDouble(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public JSONObject getJSONObject(String key) {
        try {
            return new JSONObject(getString(key));
        }catch (JSONException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an JSONObject", key);
            throw new IllegalArgumentException(key + " is not an JSONObject");
        }
    }

    public JSONObject getJSONObject(String key, JSONObject defaultValue) {
        try {
            return getJSONObject(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public JSONArray getJSONArray(String key) {
        try {
            return new JSONArray(getString(key));
        }catch (JSONException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an JSONArray", key);
            throw new IllegalArgumentException(key + " is not an JSONArray");
        }
    }

    public JSONArray getJSONArray(String key, JSONArray defaultValue) {
        try {
            return getJSONArray(key);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }
}
