package com.neo.parkguidance.ms.user.api.security.jwt;

import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPrivateKey;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;

import java.util.List;

public interface KeyService {

    /**
     * Relieve current main key
     */
    void relieveCurrentPrivateKey();

    /**
     * Revokes the current active private keys and generates a new key pair
     */
    void revokeActivePrivateKeys();


    /**
     * Returns the currently active private key
     *
     * @return private key
     */
    JWTPrivateKey getCurrentPrivateKey();

    /**
     * Returns a list of all active public Keys
     *
     * @return a list of all active public keys
     */
    List<JWTPublicKey> getActivePublicKeys();


}
