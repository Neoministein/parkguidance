package com.neo.parkguidance.core.api.validation;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.api.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.validation.EntityValidationException;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * This abstract class handles validating the base property of a {@link DataBaseEntity} and provides the dao for the
 * implementation
 * @param <T> a {@link DataBaseEntity}
 */
@Stateless
public abstract class AbstractDatabaseEntityValidation<T extends DataBaseEntity<T>> {

    @Inject
    AbstractEntityDao<T> dao;

    /**
     *
     * @param primaryKey the primary key to be checked
     */
    public void validatePrimaryKey(Object primaryKey) throws EntityValidationException {
        if (primaryKey == null) {
            throw new EntityValidationException("Key cannot be null");
        }

        if (dao.find(primaryKey) != null) {
            throw new EntityValidationException("Key already exists");
        }
    }


    protected AbstractEntityDao<T> getDao() {
        return dao;
    }
}
