package com.neo.parkguidance.core.entity;

import java.io.Serializable;

public interface DataBaseEntity<N extends DataBaseEntity<N>> extends Serializable {

    String C_ID = "id";

    Object getPrimaryKey();

    boolean compareValues(N o);
}
