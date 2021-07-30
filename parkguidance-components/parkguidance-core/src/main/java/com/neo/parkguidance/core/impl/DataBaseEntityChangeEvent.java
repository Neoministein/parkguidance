package com.neo.parkguidance.core.impl;

import com.neo.parkguidance.core.entity.DataBaseEntity;

/**
 * This is a class designed for being used for when a @{@link DataBaseEntity} has changed
 * @param <T> the {@link DataBaseEntity} which got changed
 */
public class DataBaseEntityChangeEvent<T extends DataBaseEntity<T>> {

    public static final String CREATE = "create";
    public static final String EDIT = "edit";
    public static final String REMOVE = "remove";

    private final String status;
    private final T changedObject;

    public DataBaseEntityChangeEvent(String status, T changedObject) {
        this.status = status;
        this.changedObject = changedObject;
    }

    public String getStatus() {
        return status;
    }

    public T getChangedObject() {
        return changedObject;
    }
}
