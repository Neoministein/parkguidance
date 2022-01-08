package com.neo.parkguidance.ms.user.impl.security;

public enum UserStatus {

    /**
     * If everything is ok
     */
    OK,

    /**
     * When the user requires a password reset
     */
    LOCKT_REQUIRE_PASSWORD_RESET,

    /**
     * To many failed authentications have been made
     */
    LOCKT_TO_MANY_FAILED_AUTH,

    /**
     * Account has been deactivated
     */
    DEACTIVATED
}
