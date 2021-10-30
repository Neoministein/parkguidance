package com.neo.parkguidance.core.impl.auth.credential;

import javax.security.enterprise.credential.AbstractClearableCredential;

/**
 * The credentials for authenticating thorough {@link com.neo.parkguidance.core.entity.UserToken}
 */
public class TokenCredentials extends AbstractClearableCredential {

    private String token;

    public TokenCredentials() {}

    public TokenCredentials(String token) {
        this.token = token;
    }

    @Override
    protected void clearCredential() {
        token = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
