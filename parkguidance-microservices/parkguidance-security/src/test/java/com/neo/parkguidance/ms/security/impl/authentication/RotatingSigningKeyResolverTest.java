package com.neo.parkguidance.ms.security.impl.authentication;

import com.neo.parkguidance.common.impl.http.LazyHttpCaller;
import com.neo.parkguidance.common.api.http.ResponseFormatVerification;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTKey;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class RotatingSigningKeyResolverTest extends AbstractJWTTest{

    RotatingSigningKeyResolver subject;


    static MockedStatic<LazyHttpCaller> httpCaller;

    @BeforeEach
    public void setUp() {
        httpCaller = Mockito.mockStatic(LazyHttpCaller.class);
    }

    @AfterEach
    public void close() {
        httpCaller.close();
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

    @Test
    void resolveSigningKeyNullTest() {
        //Arrange
        subject = new RotatingSigningKeyResolver("localhost", true);
        subject = Mockito.spy(subject);

        PublicKey publicKey = Mockito.mock(PublicKey.class);
        JwsHeader jwsHeader = Mockito.mock(JwsHeader.class);

        subject.keyMap = createKeyMap("TEST", publicKey, new Date());
        Mockito.doReturn(null).when(jwsHeader).getKeyId();
        //Act
        Exception exception = Assertions.assertThrows(MalformedJwtException.class, () -> subject.resolveSigningKey(jwsHeader, (Claims) null));

        //Assert
        Assertions.assertEquals("Kid header parameter is missing", exception.getMessage());
    }

    @Test
    void resolveNoNewSigningKeyTest() {
        //Arrange
        httpCaller.when(() -> LazyHttpCaller.call(Mockito.any(HttpClient.class),Mockito.any(HttpUriRequest.class),Mockito.any(
                ResponseFormatVerification.class),Mockito.anyInt()))
                .thenReturn(defaultEndpointResponse("0"));

        subject = new RotatingSigningKeyResolver("localhost", false);
        subject = Mockito.spy(subject);
        JwsHeader jwsHeader = Mockito.mock(JwsHeader.class);

        Mockito.doReturn("1").when(jwsHeader).getKeyId();
        //Act
        Exception exception = Assertions.assertThrows(
                SignatureException.class, () -> subject.resolveSigningKey(jwsHeader, (Claims) null));

        //Assert
        Assertions.assertEquals("Cannot find matching public key for kid", exception.getMessage());
    }

    @Test
    void resolveNewSigningKeyTest() {
        //Arrange
        subject = new RotatingSigningKeyResolver("localhost", true);
        subject = Mockito.spy(subject);
        JwsHeader jwsHeader = Mockito.mock(JwsHeader.class);

        Mockito.doReturn("1").when(jwsHeader).getKeyId();

        httpCaller.when(() -> LazyHttpCaller.call(Mockito.any(HttpClient.class),Mockito.any(HttpUriRequest.class),Mockito.any(
                ResponseFormatVerification.class),Mockito.anyInt()))
                .thenReturn(defaultEndpointResponse("1"));
        //Act
        Key key = subject.resolveSigningKey(jwsHeader, (Claims) null);

        //Assert
        Assertions.assertEquals(getPublicKey(), Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    @Test
    void validateEndPointParsingTest() {
        //Arrange
        subject = new RotatingSigningKeyResolver("localhost", true);
        subject = Mockito.spy(subject);

        String responseString = defaultEndpointResponse("0");

        //Act

        Map<String, JWTKey> parsed = subject.parseEndpointResult(responseString);
        //Assert
        Assertions.assertNotNull(parsed.get("0"));
    }

    protected String defaultEndpointResponse(String kid) {
        JSONObject response = new JSONObject();
        response.put("status",200);
        JSONArray data = new JSONArray();

        JSONObject key = new JSONObject();
        key.put("kid", kid);
        key.put("key", getPublicKey());
        key.put("exp",0L);
        data.put(key);
        response.put("data",data);
        return response.toString();
    }

    protected Map<String, JWTKey> createKeyMap(String kid, PublicKey publicKey, Date date) {
        Map<String, JWTKey> keyMap = new HashMap<>();
        keyMap.put(kid, new JWTPublicKey(kid, publicKey , date));
        return keyMap;
    }
}
