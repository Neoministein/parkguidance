package com.neo.parkguidance.web.infra.table;

public enum SortOrder {

    ASCENDING, DESCENDING, UNSORTED;

    public boolean isAscending() {
        return ASCENDING.equals(this);
    }
}
