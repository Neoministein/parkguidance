package com.neo.parkguidance.core.impl.auth.credential;

import javax.security.enterprise.credential.AbstractClearableCredential;

/**
 * This class holds credentials for using the {@link javax.security.enterprise.SecurityContext} for authentifaction
 */
public class OAuthCredential extends AbstractClearableCredential {

    private String token;
    private String type;

    public OAuthCredential(){}

    public OAuthCredential(String token, String type) {
        this.token = token;
        this.type = type;
    }

    @Override
    protected void clearCredential() {
        token = null;
        type = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
