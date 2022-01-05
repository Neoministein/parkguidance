package com.neo.parkguidance.ms.security.impl.authentication.key;

import java.security.Key;
import java.util.Date;

public abstract class JWTKey {

    private final String id;
    private final Key publicKey;
    private final Date expirationDate;

    protected JWTKey(String id, Key publicKey, Date expirationDate) {
        this.id = id;
        this.publicKey = publicKey;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public Key getKey() {
        return publicKey;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
