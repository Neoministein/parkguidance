package com.neo.parkguidance.parkdata.api.service;

import java.util.List;
import java.util.Map;

/**
 * This class provided a cache between the Application and Elasticsearch for the parkData
 */
public interface ParkDataService {

    /**
     * Returns all parkData in a {@link Map} with the park garage key as the key and a {@link List} containing
     * average occupied state for every half hour from 0 - 47
     *
     * @return the map
     */
    Map<String, List<Integer>> getParkData();

    /**
     * @param key the key of the desired parking garage
     * @return average occupied state for every half hour from 0 - 47
     */
    List<Integer> getParkData(String key);

    /**
     * Reloads the current parkData cache from elasticsearch
     */
    void reload();
}
