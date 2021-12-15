package com.neo.parkguidance.parkdata.impl.data;


import java.io.Serializable;
import java.util.Date;

/**
 * This object is a container for storing the ParkData in order to display it on a table
 */
public class ParkDataObject implements Serializable {

    private final String key;
    private final Integer rawSortedCount;
    private final Integer rawUnsortedCount;
    private final Integer sortedDataCount;
    private final Date lastUpdate;

    public ParkDataObject(String key, Integer rawDataCount, Integer rawUnsortedCount, Integer sortedDataCount, Date lastUpdate) {
        this.key = key;
        this.rawSortedCount = rawDataCount;
        this.rawUnsortedCount = rawUnsortedCount;
        this.sortedDataCount = sortedDataCount;
        this.lastUpdate = lastUpdate;
    }

    public String getKey() {
        return key;
    }

    public Integer getRawSortedCount() {
        return rawSortedCount;
    }

    public Integer getRawUnsortedCount() {
        return rawUnsortedCount;
    }

    public Integer getSortedDataCount() {
        return sortedDataCount;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }
}
