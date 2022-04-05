package com.neo.parkguidance.ms.user.impl.utils;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPrivateKey;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;
import com.neo.parkguidance.ms.user.impl.entity.KeyPair;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class KeyPairUtils {

    private KeyPairUtils() {}

    public static JWTPrivateKey parseToJWTPrivateKey(KeyPair keyPair) {
        return new JWTPrivateKey(
                keyPair.getId().toString(),
                KeyPairUtils.getPrivateKey(keyPair),
                keyPair.getExpirationDate());
    }

    public static JWTPublicKey parseToJWTPublicKey(KeyPair keyPair) {
        return new JWTPublicKey(
                keyPair.getId().toString(),
                KeyPairUtils.getPublicKey(keyPair),
                keyPair.getExpirationDate());
    }

    public static KeyPair generateNewKeyPair(Date expirationDate) {
        java.security.KeyPair rsaKeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

        return new KeyPair(
                Base64.getEncoder().encodeToString(rsaKeyPair.getPublic().getEncoded()),
                Base64.getEncoder().encodeToString(rsaKeyPair.getPrivate().getEncoded()),
                expirationDate);
    }

    public static PublicKey getPublicKey(KeyPair keyPair) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyPair.getPublicKey()));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalLogicException("Invalid public key parsing key format");
        } catch (InvalidKeySpecException ex) {
            throw new InternalLogicException("The public key provided cannot be parsed to a key");
        } catch (IllegalArgumentException ex) {
            throw new InternalLogicException("The public key provided is not Base64 encoded");
        }
    }

    public static PrivateKey getPrivateKey(KeyPair keyPair) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyPair.getPrivateKey()));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalLogicException("Invalid private key parsing key format");
        } catch (InvalidKeySpecException ex) {
            throw new InternalLogicException("The private key provided cannot be parsed to a key");
        } catch (IllegalArgumentException ex) {
            throw new InternalLogicException("The private key provided is not Base64 encoded");
        }
    }
}
