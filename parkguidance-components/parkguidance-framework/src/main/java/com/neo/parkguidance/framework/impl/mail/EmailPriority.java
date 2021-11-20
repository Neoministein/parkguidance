package com.neo.parkguidance.framework.impl.mail;

public enum EmailPriority {

    HIGH(1),
    NORMAL(3),
    LOW(5);

    private final int value;

    EmailPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
