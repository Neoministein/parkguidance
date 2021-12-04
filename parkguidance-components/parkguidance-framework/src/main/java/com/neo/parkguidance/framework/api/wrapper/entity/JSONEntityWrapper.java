package com.neo.parkguidance.framework.api.wrapper.entity;

import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.impl.validation.EntityValidationException;
import org.json.JSONObject;

import java.util.Collection;

public interface JSONEntityWrapper<T extends DataBaseEntity>{

    /**
     * Parses the entity to a {@link JSONObject}
     *
     * @param entity the entity to parse
     * @param callerPermissions a {@link Collection} of the callers permissions to determine what is sent
     *
     * @return the parsed {@link JSONObject}
     */
    JSONObject toJSON(T entity, Collection<String> callerPermissions);

    /**
     * Parses the JSONObject to an entity
     *
     * @param jsonObject the {@link JSONObject} to parse
     * @return the parsed entity
     *
     * @throws EntityValidationException if values are missing
     */
    T toValidEntity(JSONObject jsonObject, Collection<String> callerPermissions);

    /**
     * Parses the JSONObject to an entity
     *
     * @param jsonObject the {@link JSONObject} to parse
     * @return the parsed entity
     */
    T toOptEntity(JSONObject jsonObject);
}
