package com.neo.parkguidance.core.impl.config;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.core.impl.event.DataBaseEntityChangeEvent;
import com.neo.parkguidance.core.impl.utils.ConfigValueUtils;
import org.json.JSONArray;
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
    EntityDao<Configuration> dao;

    private Map<String, Configuration> configuration;

    @PostConstruct
    protected void init() {
        List<Configuration> allValues = dao.findAll();


        Map<String, Configuration> newMap = new HashMap<>();
        for (Configuration storedValue: allValues) {
            newMap.put(storedValue.getKey(), storedValue);
        }

        configuration = newMap;
    }

    public void configValueChangeEvent(@Observes DataBaseEntityChangeEvent<ConfigValue> changeEvent) {
        switch (changeEvent.getStatus()) {
        case DataBaseEntityChangeEvent.CREATE:
        case DataBaseEntityChangeEvent.EDIT:
            configuration.put(changeEvent.getChangedObject().getKey(), changeEvent.getChangedObject().getConfiguration());
            break;
        case DataBaseEntityChangeEvent.REMOVE:
            configuration.remove(changeEvent.getChangedObject().getKey());
            break;
        default:
            init();
        }
        LOGGER.debug("StoredValueService has been updated");
    }

    public void configurationChangeEvent(@Observes DataBaseEntityChangeEvent<Configuration> changeEvent) {
        switch (changeEvent.getStatus()) {
        case DataBaseEntityChangeEvent.CREATE:
        case DataBaseEntityChangeEvent.EDIT:
            configuration.put(changeEvent.getChangedObject().getKey(), changeEvent.getChangedObject());
            break;
        case DataBaseEntityChangeEvent.REMOVE:
            configuration.remove(changeEvent.getChangedObject().getKey());
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
        Configuration config = configuration.get(key);
        if (config == null) {
            LOGGER.warn("Unable to find the Configuration {}" , key);
            return null;
        }
        return config.getSingleValue();
    }

    public String getString(String key) {
        return ConfigValueUtils.parseString(getStoredValue(key));
    }

    public String getString(String key, String defaultValue) {
        return ConfigValueUtils.parseString(getStoredValue(key), defaultValue);
    }

    public int getInteger(String key) {
        return ConfigValueUtils.parseInteger(getStoredValue(key));
    }

    public int getInteger(String key, int defaultValue) {
        return ConfigValueUtils.parseInteger(getStoredValue(key), defaultValue);
    }

    public long getLong(String key) {
        return ConfigValueUtils.parseLong(getStoredValue(key));
    }

    public long getLong(String key, long defaultValue) {
        return ConfigValueUtils.parseLong(getStoredValue(key), defaultValue);
    }

    public boolean getBoolean(String key) {
        return ConfigValueUtils.parseBoolean(getStoredValue(key));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return ConfigValueUtils.parseBoolean(getStoredValue(key), defaultValue);
    }

    public double getDouble(String key) {
        return ConfigValueUtils.parseDouble(getStoredValue(key));
    }

    public double getDouble(String key, double defaultValue) {
        return ConfigValueUtils.parseDouble(getStoredValue(key), defaultValue);
    }

    public JSONObject getJSONObject(String key) {
        return ConfigValueUtils.parseJSONObject(getStoredValue(key));
    }

    public JSONObject getJSONObject(String key, JSONObject defaultValue) {
        return ConfigValueUtils.parseJSONObject(getStoredValue(key), defaultValue);
    }

    public JSONArray getJSONArray(String key) {
        return ConfigValueUtils.parseJSONArray(getStoredValue(key))
;    }

    public JSONArray getJSONArray(String key, JSONArray defaultValue) {
        return ConfigValueUtils.parseJSONArray(getStoredValue(key), defaultValue);
    }

    @Override
    public Map<String, ConfigValue> getConfigMap(String key) {
        Configuration map = configuration.get(key);
        if (map == null) {
            throw new IllegalArgumentException("Configuration object is null");
        }
        return configuration.get(key).getMap();
    }
}
