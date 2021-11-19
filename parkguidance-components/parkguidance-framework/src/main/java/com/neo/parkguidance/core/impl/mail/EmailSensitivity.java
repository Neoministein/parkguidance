package com.neo.parkguidance.core.impl.mail;

public enum EmailSensitivity {
    NORMAL(""),
    PERSONAL("Personal"),
    PRIVATE("Private"),
    COMPANY_CONFIDENTIAL("Company-Confidential");

    private final String value;

    EmailSensitivity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
