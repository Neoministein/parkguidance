package com.neo.parkguidance.ms.user.impl.security.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class RotatingSigningKeyResolverTest {


    RotatingSigningKeyResolver subject;

    @BeforeEach
    public void setUp() {

    }

    @Test
    void resolveSigningKeyValid() {
        //Arrange
        subject = new RotatingSigningKeyResolver("localhost", true);
        subject = Mockito.spy(subject);

        PublicKey publicKey = Mockito.mock(PublicKey.class);
        JwsHeader jwsHeader = Mockito.mock(JwsHeader.class);

        subject.keyMap = createKeyMap("TEST", publicKey, new Date());
        Mockito.doReturn("TEST").when(jwsHeader).getKeyId();
        //Act
        Key result = subject.resolveSigningKey(jwsHeader, (Claims) null);

        //Assert
        Assertions.assertEquals(publicKey, result);
    }
/*
    @Test
    void resolveSigningKeyNotValid() {
        //Arrange
        subject = new RotatingSigningKeyResolver("localhost", true);
        subject = Mockito.spy(subject);

        PublicKey publicKey = Mockito.mock(PublicKey.class);
        JwsHeader jwsHeader = Mockito.mock(JwsHeader.class);

        subject.keyMap = createKeyMap("TEST", publicKey, new Date());
        Mockito.doReturn("INVALID").when(jwsHeader).getKeyId();
        //Act
        Key result = subject.resolveSigningKey(jwsHeader, (Claims) null);

        //Assert
        Assertions.assertEquals(publicKey, result);
    }

 */


    protected Map<String, JWTPublicKey> createKeyMap(String kid, PublicKey publicKey, Date date) {
        Map<String, JWTPublicKey> keyMap = new HashMap<>();
        keyMap.put(kid, new JWTPublicKey(kid, publicKey , date));
        return keyMap;
    }
}
