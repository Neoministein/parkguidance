package com.neo.parkguidance.ms.security.impl.authentication.key;

import java.security.PrivateKey;
import java.util.Date;

public class JWTPrivateKey extends JWTKey{

    public JWTPrivateKey(String id, PrivateKey publicKey, Date expirationDate) {
        super(id, publicKey, expirationDate);
    }

    @Override
    public PrivateKey getKey() {
        return (PrivateKey) super.getKey();
    }
}
