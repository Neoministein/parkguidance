package com.neo.parkguidance.core.entity;

import javax.persistence.*;

/**
 * This entity class is used for storing persistent data like in {@link java.util.Map.Entry}.
 * This data can change from environment to environment like:
 * - API access keys
 */
@Entity
@Table(name = StoredValue.TABLE_NAME)
public class StoredValue implements DataBaseEntity<StoredValue> {

    public static final String TABLE_NAME = "storedValue";

    public static final String C_KEY = "key";
    public static final String C_VALUE = "value";
    public static final String C_HIDDEN = "hidden";
    public static final String C_COMPONENT = "component";
    public static final String C_DESCRIPTION = "description";


    public static final String V_GOOGLE_MAPS_API = "cloud.google.maps.api.key";

    @Id
    @Column(name = C_KEY)
    private String key;

    @Column(name = C_VALUE, nullable = false)
    private String value;

    @Column(name = C_HIDDEN, nullable = false)
    private Boolean hidden;

    @Column(name = C_COMPONENT)
    private String component;

    @Column(name = C_DESCRIPTION)
    private String description;

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

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Object getPrimaryKey() {
        return getKey();
    }

    @Override
    public boolean compareValues(StoredValue o) {
        if(!key.equals(o.getKey())) {
            return false;
        }
        return value.equals(o.getValue());
    }
}
