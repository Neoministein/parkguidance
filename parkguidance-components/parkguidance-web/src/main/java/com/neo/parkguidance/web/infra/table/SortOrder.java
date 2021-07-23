package com.neo.parkguidance.web.infra.table;

/**
 * This enum describes the sort order of a table
 */
public enum SortOrder {

    ASCENDING, DESCENDING, UNSORTED;

    public boolean isAscending() {
        return ASCENDING.equals(this);
    }

    public boolean isDescending() {
        return DESCENDING.equals(this);
    }

    public boolean isUnsorted() {
        return UNSORTED.equals(this);
    }
}
