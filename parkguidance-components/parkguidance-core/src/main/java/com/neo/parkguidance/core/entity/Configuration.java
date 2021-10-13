package com.neo.parkguidance.core.entity;

import com.neo.parkguidance.core.impl.config.ConfigType;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * This entity class is used for storing persistent configuration with either a single value or a {@link java.util.Map}.
 * This data can change from environment to environment like:
 * - API access keys
 */
@Entity
@Table(name = Configuration.TABLE_NAME)
public class Configuration implements DataBaseEntity{

    public static final String TABLE_NAME = "configuration";

    public static final String C_KEY = "key";
    public static final String C_TYPE = "type";
    public static final String C_COMPONENT = "component";
    public static final String C_DESCRIPTION = "description";

    @Id
    @Column(name = C_KEY)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = C_TYPE, nullable = false)
    private ConfigType type;

    @Column(name = C_COMPONENT)
    private String component;

    @Column(name = C_DESCRIPTION)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ConfigValue> configValues;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ConfigType getType() {
        return type;
    }

    public void setType(ConfigType type) {
        this.type = type;
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

    public List<ConfigValue> getConfigValues() {
        return configValues;
    }

    public void setConfigValues(List<ConfigValue> configValues) {
        this.configValues = configValues;
    }

    @Override
    public Object getPrimaryKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Configuration that = (Configuration) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
