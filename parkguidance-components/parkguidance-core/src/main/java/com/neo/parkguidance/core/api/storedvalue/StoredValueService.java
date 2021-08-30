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
     *
     * @return the {@link StoredValue} object
     */
    StoredValue getStoredValue(String key);

    /**
     * Returns the {@link StoredValue} value associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found
     *
     * @param key the {@link StoredValue} key
     *
     * @return
     */
    String getString(String key);

    /**
     * Returns the {@link StoredValue} value associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *
     * @return
     */
    String getString(String key, String defaultValue);

    /**
     * Returns the {@link StoredValue} value cast to a int associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found or cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    int getInteger(String key);

    /**
     * Returns the {@link StoredValue} value cast to a int associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *
     * @return
     */
    int getInteger(String key, int defaultValue);

    /**
     * Returns the {@link StoredValue} value cast to a int associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found or cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    long getLong(String key);

    /**
     * Returns the {@link StoredValue} value cast to a int associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *
     * @return
     */
    long getLong(String key, long defaultValue);

    /**
     * Returns the {@link StoredValue} value cast to a boolean associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    boolean getBoolean(String key);

    /**
     * Returns the {@link StoredValue} value cast to a boolean associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *
     * @return
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Returns the {@link StoredValue} value cast to a double associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     *
     * @return
     */
    double getDouble(String key);

    /**
     * Returns the {@link StoredValue} value cast to a double associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *
     * @return
     */
    double getDouble(String key, double defaultValue);

    /**
     * Returns the {@link StoredValue} value cast to a JSONObject associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     *
     * @return
     */
    JSONObject getJSONObject(String key);

    /**
     * Returns the {@link StoredValue} value cast to a JSONObject associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *
     * @return
     */
    JSONObject getJSONObject(String key, JSONObject defaultValue);

    /**
     * Returns the {@link StoredValue} value cast to a JSONArray associated to the key <br>
     * throws an {@link IllegalArgumentException} if not found cannot cast
     *
     * @param key the {@link StoredValue} key
     * @return
     */
    JSONArray getJSONArray(String key);

    /**
     * Returns the {@link StoredValue} value cast to a JSONArray associated to the key <br>
     *
     * @param key the {@link StoredValue} key
     * @param defaultValue the default value which will be returned if none are found or cannot be parsed
     *                     
     * @return
     */
    JSONArray getJSONArray(String key, JSONArray defaultValue);
}
