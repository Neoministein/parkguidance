package com.neo.parkguidance.framework.impl.event;

import com.neo.parkguidance.framework.entity.DataBaseEntity;

/**
 * This is a class designed for being used for when a @{@link DataBaseEntity} has changed
 * @param <T> the {@link DataBaseEntity} which got changed
 */
public class DataBaseEntityChangeEvent<T extends DataBaseEntity> extends ChangeEventImpl {

    public static final String CREATE = "create";
    public static final String EDIT = "edit";
    public static final String REMOVE = "remove";

    private final String status;
    private final T changedObject;

    public DataBaseEntityChangeEvent(String status, T changedObject) {
        super(DataBaseEntityChangeEvent.class.getName());
        this.status = status;
        this.changedObject = changedObject;
    }

    public DataBaseEntityChangeEvent(String status ,String type, T changedObject) {
        super(type);
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
