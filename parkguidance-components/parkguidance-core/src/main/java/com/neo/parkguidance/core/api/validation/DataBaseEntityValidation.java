package com.neo.parkguidance.core.api.validation;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.impl.validation.EntityValidationException;

/**
 * This interface is a base file for {@link DataBaseEntity} validation
 * @param <T>
 */
public interface DataBaseEntityValidation<T extends DataBaseEntity<T>> {

    /**
     * Validates the primary key and checks if it isn't null
     *
     * @param primaryKey the primary key that should be checked
     *
     * @throws EntityValidationException if the value is null
     */
    void validatePrimaryKey(Object primaryKey) throws EntityValidationException;
}
