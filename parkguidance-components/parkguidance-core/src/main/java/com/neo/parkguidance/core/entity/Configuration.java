package com.neo.parkguidance.core.entity;

import com.neo.parkguidance.core.impl.config.ConfigType;

import javax.persistence.*;
import java.util.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = C_KEY, nullable = false, unique = true)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = C_TYPE, nullable = false)
    private ConfigType type;

    @Column(name = C_COMPONENT)
    private String component;

    @Column(name = C_DESCRIPTION)
    private String description;

    @OneToMany(mappedBy = TABLE_NAME, orphanRemoval = true,fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ConfigValue> configValues;

    public Configuration() {
        configValues = new ArrayList<>();
    }

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

    public Map<String, ConfigValue> getMap() {
        if (getType() != ConfigType.MAP) {
            throw new IllegalArgumentException(getKey() + " is a ConfigType." + getType().toString());
        }
        Map<String, ConfigValue> map = new HashMap<>();
        for (ConfigValue configValue : configValues) {
            map.put(configValue.getKey(), configValue);
        }
        return map;
    }

    public ConfigValue getSingleValue() {
        if (getType() != ConfigType.SINGLE) {
            throw new IllegalArgumentException(getKey() + " is a ConfigType." + getType().toString());
        } else if (!configValues.isEmpty()) {
            return configValues.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Object getPrimaryKey() {
        return getId();
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
