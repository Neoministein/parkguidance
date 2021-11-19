package com.neo.parkguidance.core.api.validation;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.impl.validation.EntityValidationException;

/**
 * This interface is a base file for {@link DataBaseEntity} validation
 */
public interface DataBaseEntityValidation<T extends DataBaseEntity> {

    /**
     * Validates the primary key and checks if it isn't null
     *
     * @param primaryKey the primary key that should be checked
     *
     * @throws EntityValidationException if the value is null
     */
    void validatePrimaryKey(Object primaryKey) throws EntityValidationException;

    /**
     * Validates if any non primary key values have change
     *
     * @param entity the entity to check
     *
     * @return true if the object or primaryKey is null or the values have not changed </b>
     *          false if values have change or it cannot be found
     *
     */
    boolean hasNothingChanged(T entity);
}
