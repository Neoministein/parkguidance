package com.neo.parkguidance.core.impl;

import com.neo.parkguidance.core.entity.DataBaseEntity;

/**
 * This is a class designed for being used for when a @{@link DataBaseEntity} has changed
 * @param <T> the {@link DataBaseEntity} which got changed
 */
public class DataBaseEntityChangeEvent<T extends DataBaseEntity<T>> extends ChangeDataEvent {

    public static final String CREATE = "create";
    public static final String EDIT = "edit";
    public static final String REMOVE = "remove";

    private final String type;
    private final T changedObject;

    public DataBaseEntityChangeEvent(String type, T changedObject) {
        super(DataBaseEntityChangeEvent.class.getName());
        this.type = type;
        this.changedObject = changedObject;
    }

    public DataBaseEntityChangeEvent(String status ,String type, T changedObject) {
        super(status);
        this.type = type;
        this.changedObject = changedObject;
    }

    public String getType() {
        return type;
    }

    public T getChangedObject() {
        return changedObject;
    }
}
