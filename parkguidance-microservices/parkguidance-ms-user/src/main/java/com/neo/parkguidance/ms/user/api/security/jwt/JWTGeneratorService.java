package com.neo.parkguidance.ms.user.api.security.jwt;

import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import org.json.JSONObject;

public interface JWTGeneratorService {

    JSONObject generateJWTResponse(RegisteredUser registeredUser, boolean generateToken , boolean restricted);
}
