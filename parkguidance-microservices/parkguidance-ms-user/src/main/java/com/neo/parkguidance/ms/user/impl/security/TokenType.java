package com.neo.parkguidance.ms.user.impl.security;

import com.neo.parkguidance.ms.user.impl.entity.UserToken;

/**
 * This class defines the type of {@link UserToken}
 */
public enum TokenType {

    /**
     * A token used for partial authentication for example none authenticated E-Mail
     */
    PARTIAL,

    /**
     * A token which will be used to verify 3rd party services like E-Mail
     */
    VERIFICATION,

    /**
     * A one time token which will be deleted after one use
     */
    ONE_TIME,

    /**
     * A token used for API calls which shouldn't expire
     */
    API,

    /**
     * A token used for refreshing a clients identity
     */
    REFRESH

}
