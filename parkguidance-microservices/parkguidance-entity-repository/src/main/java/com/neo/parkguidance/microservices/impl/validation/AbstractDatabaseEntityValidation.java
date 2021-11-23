package com.neo.parkguidance.microservices.impl.validation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.api.validation.DataBaseEntityValidation;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.impl.utils.StringUtils;

import javax.inject.Inject;

/**
 * This abstract class handles validating the base property of a {@link DataBaseEntity} and provides the dao for the
 * implementation
 * @param <T> a {@link DataBaseEntity}
 */
public abstract class AbstractDatabaseEntityValidation<T extends DataBaseEntity> implements
        DataBaseEntityValidation<T> {

    @Inject
    EntityDao<T> dao;

    /**
     *
     * @param primaryKey the primary key to be checked
     */
    public void validatePrimaryKey(Object primaryKey) {
        if (primaryKey == null) {
            throw new EntityValidationException("Key cannot be null");
        }
        if (primaryKey instanceof String && StringUtils.isEmpty(primaryKey.toString())) {
            throw new EntityValidationException("A String Key cannot be empty");
        }

        if (dao.find(primaryKey) != null) {
            throw new EntityValidationException("Key already exists");
        }
    }

    protected boolean uniqueValue(String column ,Object value){
        return dao.findOneByColumn(column, value) != null;
    }

    protected T returnOriginalObject(T entity) {
        if(entity != null && entity.getPrimaryKey() != null) {
            return dao.find(entity.getPrimaryKey());
        }
        return null ;
    }

    protected boolean valueExists(String column, String value) {
        return dao.findOneByColumn(column, value) != null;
    }

    protected EntityDao<T> getDao() {
        return dao;
    }
}
