package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.core.entity.DataBaseEntity;

import java.io.Serializable;

public abstract class AbstractFormModel<T extends DataBaseEntity> implements Serializable {

    private boolean initialized = false;
    private Object primaryKey;
    private T entity;

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

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
