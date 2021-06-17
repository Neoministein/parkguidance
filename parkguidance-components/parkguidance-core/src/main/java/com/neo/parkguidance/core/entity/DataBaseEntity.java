package com.neo.parkguidance.core.entity;

import java.io.Serializable;

public interface DataBaseEntity<T> extends Serializable {

    String C_ID = "id";

    Object getPrimaryKey();

    boolean compareValues(T o);
}
