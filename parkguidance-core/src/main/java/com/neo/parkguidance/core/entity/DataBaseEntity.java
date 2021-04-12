package com.neo.parkguidance.core.entity;

import java.io.Serializable;

public interface DataBaseEntity extends Serializable {

    String C_ID = "id";

    Long getId();

    void setId(Long id);
}
