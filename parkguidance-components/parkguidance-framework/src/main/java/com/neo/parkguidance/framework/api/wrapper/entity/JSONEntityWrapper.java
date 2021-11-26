package com.neo.parkguidance.framework.api.wrapper.entity;

import com.neo.parkguidance.framework.entity.DataBaseEntity;
import org.json.JSONObject;

public interface JSONEntityWrapper<T extends DataBaseEntity>{

    /**
     * Parses the entity to a {@link JSONObject}
     *
     * @param entity the entity to parse
     * @param hideValues confirms if values shouldn't be parsed due to it being an internal only resource
     *
     * @return the parsed {@link JSONObject}
     */
    JSONObject toJSON(T entity, boolean hideValues);

    /**
     * Parses the JSONObject to an entity
     *
     * @param jsonObject the {@link JSONObject} to parse
     * @return the parsed entity
     */
    T toEntity(JSONObject jsonObject);
}
