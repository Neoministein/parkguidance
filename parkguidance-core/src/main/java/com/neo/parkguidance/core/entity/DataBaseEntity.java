package com.neo.parkguidance.core.entity;

import java.io.Serializable;

public interface DataBaseEntity<T> extends Serializable {

    String C_ID = "id";

    Long getId();

    void setId(Long id);

    boolean compareValues(T o);
}
