package com.neo.parkguidance.core.api.storedvalue;

import com.neo.parkguidance.core.entity.StoredValue;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class provided a cache between the Application and the Database for the {@link StoredValue} entity
 * The class also handles casting and non extant primary key
 */
public interface StoredValueService {

    /**
     * Reloads all {@link StoredValue} from the database
     */
    void reload();

    /**
     * Returns the {@link StoredValue} associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found
     *
     * @param key the {@link StoredValue} key
     * @return the {@link StoredValue} object
     */
    StoredValue getStoredValue(String key);

    /**
     * Returns the {@link StoredValue} value associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    String getString(String key);

    /**
     * Returns the {@link StoredValue} value cast to a int associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found or cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    int getInteger(String key);

    /**
     * Returns the {@link StoredValue} value cast to a boolean associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    boolean getBoolean(String key);

    /**
     * Returns the {@link StoredValue} value cast to a double associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    double getDouble(String key);

    /**
     * Returns the {@link StoredValue} value cast to a JSONObject associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    JSONObject getJSONObject(String key);

    /**
     * Returns the {@link StoredValue} value cast to a JSONArray associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    JSONArray getJSONArray(String key);
}
