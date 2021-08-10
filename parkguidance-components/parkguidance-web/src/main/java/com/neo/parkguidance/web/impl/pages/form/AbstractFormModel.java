package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.core.entity.DataBaseEntity;

import java.io.Serializable;

public abstract class AbstractFormModel<T extends DataBaseEntity<T>> implements Serializable {

    private Object primaryKey;
    private T entity;

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
