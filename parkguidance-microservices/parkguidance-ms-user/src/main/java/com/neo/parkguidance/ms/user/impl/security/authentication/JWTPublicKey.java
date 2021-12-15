package com.neo.parkguidance.ms.user.impl.security.authentication;

import java.security.PublicKey;
import java.util.Date;

public class JWTPublicKey {

    private final String id;
    private final PublicKey publicKey;
    private final Date expirationDate;

    public JWTPublicKey(String id, PublicKey publicKey, Date expirationDate) {
        this.id = id;
        this.publicKey = publicKey;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
