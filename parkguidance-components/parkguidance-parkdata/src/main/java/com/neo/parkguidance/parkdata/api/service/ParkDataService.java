package com.neo.parkguidance.parkdata.api.service;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.parkdata.impl.data.ParkDataObject;

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
     * sorts the raw unsorted data
     */
    void sortParkData();

    /**
     * Deletes the old sorted ParkData
     * @throws RuntimeException on fail
     */
    void deleteRawSortedData();

    /**
     * Gets the parkData associated with the {@link ParkingGarage}
     *
     * @param parkingGarage
     * @return the time it has been last updated
     */
    ParkDataObject getMetaData(ParkingGarage parkingGarage);

    /**
     * Reloads the current parkData cache from elasticsearch
     */
    void reload();
}
