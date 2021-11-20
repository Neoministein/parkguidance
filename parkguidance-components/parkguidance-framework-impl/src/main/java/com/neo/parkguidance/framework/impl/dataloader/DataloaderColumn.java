package com.neo.parkguidance.framework.impl.dataloader;

/**
 * This class contains the name and the type of data in a column of a table
 */
public class DataloaderColumn {

    private final String value;
    private final String dataType;

    public DataloaderColumn(String value, String dataType) {
        this.value = value;
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public String getDataType() {
        return dataType;
    }
}
