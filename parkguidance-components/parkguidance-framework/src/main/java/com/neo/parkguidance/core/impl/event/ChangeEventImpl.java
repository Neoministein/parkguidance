package com.neo.parkguidance.core.impl.event;

import com.neo.parkguidance.core.api.event.ChangeEvent;

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
