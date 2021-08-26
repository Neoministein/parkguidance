package com.neo.parkguidance.core.impl.storedvalue;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.api.dao.AbstractEntityDao;
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
 * The {@link StoredValueService} implementation
 */
@ApplicationScoped
public class StoredValueServiceImpl implements StoredValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoredValueServiceImpl.class);

    @Inject
    AbstractEntityDao<StoredValue> dao;

    private Map<String, StoredValue> storedValues;

    @PostConstruct
    protected void init() {
        List<StoredValue> allValues = dao.findAll();


        Map<String, StoredValue> newMap = new HashMap<>();
        for (StoredValue storedValue: allValues) {
            newMap.put(storedValue.getKey(), storedValue);
        }

        storedValues = newMap;
    }

    public void changeEvent(@Observes DataBaseEntityChangeEvent<StoredValue> changeEvent) {
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

    public StoredValue getStoredValue(String key) {
        StoredValue storedValue = storedValues.get(key);
        if (storedValue == null) {
            LOGGER.error("Unable to find the stored value {}" , key);
            throw new IllegalArgumentException(getClass().getName() + " has no entry for the key " + key);
        }
        return storedValue;
    }

    public String getString(String key) {
        return getStoredValue(key).getValue();
    }

    public int getInteger(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Integer", key);
            throw new IllegalArgumentException(key + " is not an Integer");
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

    public double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an Double", key);
            throw new IllegalArgumentException(key + " is not an Double");
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

    public JSONArray getJSONArray(String key) {
        try {
            return new JSONArray(getString(key));
        }catch (JSONException ex) {
            LOGGER.warn("Wrong type specified [{}] is not an JSONArray", key);
            throw new IllegalArgumentException(key + " is not an JSONArray");
        }
    }
}
