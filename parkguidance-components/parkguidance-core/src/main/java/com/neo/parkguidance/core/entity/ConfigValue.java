package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * This entity stored the values for a {@link Configuration}
 */
@Entity
@Table(name = ConfigValue.TABLE_NAME)
public class ConfigValue implements DataBaseEntity {

    public static final String TABLE_NAME = "configValue";

    public static final String C_KEY = "key";
    public static final String C_VALUE = "value";
    public static final String C_HIDDEN = "hidden";
    public static final String C_DESCRIPTION = "description";


    public static final String V_GOOGLE_MAPS_API = "cloud.google.maps.api.key";
    public static final String V_GOOGLE_MAPS_API_EXTERNAL = "cloud.google.maps.api.key.external";

    @Id
    @Column(name = C_KEY)
    private String key;

    @Column(name = C_VALUE, nullable = false)
    private String value;

    @Column(name = C_HIDDEN, nullable = false)
    private Boolean hidden;

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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConfigValue that = (ConfigValue) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
