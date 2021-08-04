package com.neo.parkguidance.core.impl;

public class ChangeDataEvent {

    private final String status;

    public ChangeDataEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
