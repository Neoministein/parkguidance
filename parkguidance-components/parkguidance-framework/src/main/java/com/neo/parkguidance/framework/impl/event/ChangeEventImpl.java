package com.neo.parkguidance.framework.impl.event;

import com.neo.parkguidance.framework.api.event.ChangeEvent;

/**
 * This class is a container for {@link javax.enterprise.event.Event}
 */
public class ChangeEventImpl implements ChangeEvent {

    private final String type;

    public ChangeEventImpl(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
