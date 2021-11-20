package com.neo.parkguidance.framework.impl.security.oauth2;

import com.neo.parkguidance.framework.api.security.oauth2.OAuth2Client;

/**
 * The Object that an {@link OAuth2Client} returns on successful authentication
 */
public class OAuth2ClientObject {

    private final String clientId;
    private final String username;
    private final String email;
    private final String picture;

    public OAuth2ClientObject(String clientId, String username, String email, String picture) {
        this.clientId = clientId;
        this.username = username;
        this.email = email;
        this.picture = picture;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }
}
