package com.neo.parkguidance.ms.security.impl.authentication.key;

import java.security.Key;
import java.util.Date;

public abstract class JWTKey {

    private final String id;
    private final Key key;
    private final Date expirationDate;

    protected JWTKey(String id, Key key, Date expirationDate) {
        this.id = id;
        this.key = key;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public Key getKey() {
        return key;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
