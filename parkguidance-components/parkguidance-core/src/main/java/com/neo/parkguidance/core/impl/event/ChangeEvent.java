package com.neo.parkguidance.core.impl.event;

/**
 * This class is a container for {@link javax.enterprise.event.Event}
 */
public class ChangeEvent {

    private final String type;

    public ChangeEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
