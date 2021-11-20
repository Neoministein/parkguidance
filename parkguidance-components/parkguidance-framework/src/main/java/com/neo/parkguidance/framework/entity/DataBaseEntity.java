package com.neo.parkguidance.framework.entity;

import java.io.Serializable;

/**
 * A Interface which enables easy working with unknown database entities
 */
public interface DataBaseEntity extends Serializable {

    String C_ID = "id";

    Object getPrimaryKey();
}
