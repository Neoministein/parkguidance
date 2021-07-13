package com.neo.parkguidance.core.entity;

import javax.persistence.*;

@Entity
@Table(name = StoredValue.TABLE_NAME)
public class StoredValue implements DataBaseEntity<StoredValue> {

    public static final String TABLE_NAME = "storedValue";

    public static final String C_KEY = "key";
    public static final String C_VALUE = "value";

    public static final String V_GOOGLE_MAPS_API = "cloud.google.maps.api";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_KEY, unique = true, nullable = false)
    private String key;

    @Column(name = C_VALUE, nullable = false)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public Object getPrimaryKey() {
        return getId();
    }

    @Override
    public boolean compareValues(StoredValue o) {
        if(!key.equals(o.getKey())) {
            return false;
        }
        return value.equals(o.getValue());
    }
}
