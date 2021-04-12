package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = StoredValue.TABLE_NAME)
public class StoredValue implements DataBaseEntity{

    public static final String TABLE_NAME = "storedValue";

    public static final String C_KEY = "key";
    public static final String C_VALUE = "value";

    public static final String V_GOOGLE_MAPS_API = "console.cloud.google.maps.api";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_KEY, unique = true)
    @NotNull
    private String key;

    @Column(name = C_VALUE)
    @NotNull
    private String value;

    @Override public Long getId() {
        return id;
    }

    @Override public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
