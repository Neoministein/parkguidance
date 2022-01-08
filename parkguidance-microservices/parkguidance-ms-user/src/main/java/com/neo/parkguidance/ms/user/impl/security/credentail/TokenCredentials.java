package com.neo.parkguidance.ms.user.impl.security.credentail;

import java.util.Date;

/**
 * The credentials for authenticating thorough {@link com.neo.parkguidance.ms.user.impl.entity.UserToken}
 */
public class TokenCredentials extends AbstractLoginCredentials {

    private final String token;

    public TokenCredentials(String token ,String ipAddress,String endpoint) {
        super(new Date(),ipAddress,endpoint);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
