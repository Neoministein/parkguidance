package com.neo.parkguidance.common.impl.util;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    private KeyUtils() {}

    /**
     * Parses a base64 encoded public key to a key instance
     *
     * @param base64Key the base64 encoded key
     * @return a public key instance
     *
     * @throws InternalLogicException if the key cannot be parsed
     */
    public static PublicKey parseRSAPublicKey(String base64Key) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64Key));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalLogicException("Invalid public key parsing key format");
        } catch (InvalidKeySpecException ex) {
            throw new InternalLogicException("The public key cannot be parsed to a key");
        } catch (IllegalArgumentException ex) {
            throw new InternalLogicException("The public key is not Base64 encoded");
        }
    }

    /**
     * Parses a base64 encoded private key to a key instance
     *
     * @param base64Key the base64 encoded key
     * @return a private key instance
     */
    public static PrivateKey parseRSAPrivateKey(String base64Key) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64Key));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalLogicException("Invalid private key parsing key format");
        } catch (InvalidKeySpecException ex) {
            throw new InternalLogicException("The private key cannot be parsed to a key");
        } catch (IllegalArgumentException ex) {
            throw new InternalLogicException("The private key is not Base64 encoded");
        }
    }
}
