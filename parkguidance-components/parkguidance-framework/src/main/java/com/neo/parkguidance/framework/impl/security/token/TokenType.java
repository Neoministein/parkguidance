package com.neo.parkguidance.framework.impl.security.token;

/**
 * This class defines the type of {@link com.neo.parkguidance.framework.entity.UserToken}
 */
public enum  TokenType {

    /**
     * A one time token which will be deleted after one use
     */
    ONE_TIME,

    /**
     * A token used for API calls
     */
    API,

    /**
     * Unlimited authentication token
     */
    UNLIMITED

}
