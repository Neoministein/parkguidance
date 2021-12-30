package com.neo.parkguidance.ms.security.impl.authentication;

import com.neo.parkguidance.common.impl.http.LazyHttpCaller;
import com.neo.parkguidance.common.impl.http.verify.ResponseFormatVerification;
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

class RotatingSigningKeyResolverTest {

    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlxfBorPoJuiDTnmih8daK+ERlniVxVPK+KMD3BbWf9CaoZkxTxcbz0ikxrGXUMKWscYm6l08lh7M8UioMfQpxSiI1dFeCLlKkcrkr45Gi1fcNFWxeTcVxoVDXLmW04QnREBQVrrrE64DKPWepHjZYhGUs1ziZvpJ4gPVBo5hAEHROnZrP/axlk5QCui2I+4U/DLkiXYQInsFY4xycPgJQjQnwypoDOk5BwiaUQlYACbSI9I5hjAnSS53AgdD5J5AerB0uD4uchmvipUngjmtgT5NWLMfwX8Um4Q7655YfrLIQB7UQVlbukX6UqlyeVBGB145YXqc+DQmzhfFb9TUbwIDAQAB";
    private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXF8Gis+gm6INOeaKHx1or4RGWeJXFU8r4owPcFtZ/0JqhmTFPFxvPSKTGsZdQwpaxxibqXTyWHszxSKgx9CnFKIjV0V4IuUqRyuSvjkaLV9w0VbF5NxXGhUNcuZbThCdEQFBWuusTrgMo9Z6keNliEZSzXOJm+kniA9UGjmEAQdE6dms/9rGWTlAK6LYj7hT8MuSJdhAiewVjjHJw+AlCNCfDKmgM6TkHCJpRCVgAJtIj0jmGMCdJLncCB0PknkB6sHS4Pi5yGa+KlSeCOa2BPk1Ysx/BfxSbhDvrnlh+sshAHtRBWVu6RfpSqXJ5UEYHXjlhepz4NCbOF8Vv1NRvAgMBAAECggEAC1AZDICh8j/YjQiodbgkdUKd2YFf3W1/tgwmntUK7HkHCvB4c66MQ4Hx7HqhlMdtFxR6F3QxtB/MQoipJ0xb0EpPrIrHmqmc/ruNUeO6PnauozCDYEsSk1GpEIlI1L+BXSffnA7UyR1vWSLC5RezVxdv5dofjYxR+d1Flv8UHjux5jFjgx1w/qmO79KdoWjxvk5nxs9fCufGuog27V67w9gicLRYTKOLqZa+0WqWq5WNsEDxateNcG/Y2CxGVNlEGo2yth3CCq6GrSZ6v2Ul/vEdxejWI1lIBTGhP2d1a9U+8CymI32FLgQPgBshKLktccYS0YoVIhj3mFMv8f9rYQKBgQDEx2jnZ41rXvJvxZrKqPlfCNYavf2niCnJamkTj6QhGTGfZQ//N+Lfrv7cuINIwviD4shwPjxu6J5z6V16WHw75f/FDsSc395kjzGERb0llZ1AU7g1tPOHBZinDD737H/fVQHo6goJ4oQM+o4Q8edGlodEktVMA7AHMZhibZh+XwKBgQDEkIOXihseCiaG1LEfzktOOQ3m/7zCbP2fz6pqbndgLJAOWXihBT0CAPHF70MEHiC6JwZBfiibNuponmv9w3RYYDeVvjbVoW+MhlzpuZiuuwAewYtRwQhgpGPCT6iU1KENrTVRlnQrUCd51I12t8EoMDnJyW8TfTJ+LlM8jK1D8QKBgCHCxLDPB2hghd+/W2thykcP0QwNhLN1nRW9eAGvJ022cNkUEcaL5BTUhU2BKqUVLmYSRqlbRCsTJfxlEXCoNchhEZzRh9ISZqmCF9DF1knYfX5H3tb4EMAdqa8fXr5SiS3b+9PmM4q2nklLw4wMtM1bjiGpv4anK1BQpgBdJUipAoGBAKhdfEml4tpVKXDd56VtpzUmS5EsRjtm7csgLkEhw6CeKvoPivKpMPtxYaX/7bAuOiwY1xPsAs4IOLpJ0Ag5jYNRTus1VXSg5ZJBWH5eGbi4tj6h2zq4k/trc4Nxhd2NTzIOxZfPvkuHSXjiJhWlCY7p1+XOYxUXbNOFzIgVmyeRAoGAbivQRcn2TIfHZDp2RuXgmYcLKeV4edCidL3FsqbNg/KAgWm5Ya7VXBqteoO76v1JKljgX29+Z+TJUKa2MO6xbDwqOu+uKiYxC66j7zKgIgMTv/oXlnOe0mE5VfwayAncI3AlWR0sYJUgW7WtGESk7jUR7HlWkbWl30FOrhIV0E8=";

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
        Assertions.assertEquals(PUBLIC_KEY, Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    @Test
    void validateEndPointParsingTest() {
        //Arrange
        subject = new RotatingSigningKeyResolver("localhost", true);
        subject = Mockito.spy(subject);

        String responseString = defaultEndpointResponse("0");

        //Act

        Map<String, JWTPublicKey> parsed = subject.parseEndpointResult(responseString);
        //Assert
        Assertions.assertNotNull(parsed.get("0"));
    }

    protected String defaultEndpointResponse(String kid) {
        JSONObject response = new JSONObject();
        response.put("status",200);
        JSONArray data = new JSONArray();

        JSONObject key = new JSONObject();
        key.put("kid", kid);
        key.put("key", PUBLIC_KEY);
        key.put("exp",0l);
        data.put(key);
        response.put("data",data);
        return response.toString();
    }

    protected Map<String, JWTPublicKey> createKeyMap(String kid, PublicKey publicKey, Date date) {
        Map<String, JWTPublicKey> keyMap = new HashMap<>();
        keyMap.put(kid, new JWTPublicKey(kid, publicKey , date));
        return keyMap;
    }
}
