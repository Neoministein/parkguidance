package com.neo.parkguidance.core.api.auth;

import com.neo.parkguidance.core.impl.auth.OAuth2ClientObject;

public interface OAuth2Client {

    OAuth2ClientObject verifyToken(String token);

    String getProvider();

    void reload();

    String renderXhtml();

}
