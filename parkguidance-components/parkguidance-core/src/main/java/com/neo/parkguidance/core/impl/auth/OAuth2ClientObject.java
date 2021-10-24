package com.neo.parkguidance.core.impl.auth;

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
