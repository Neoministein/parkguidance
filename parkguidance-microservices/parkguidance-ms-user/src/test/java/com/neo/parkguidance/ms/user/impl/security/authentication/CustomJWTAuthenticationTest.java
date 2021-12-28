package com.neo.parkguidance.ms.user.impl.security.authentication;

import io.helidon.security.AuthenticationResponse;
import io.helidon.security.ProviderRequest;
import io.helidon.security.SecurityContext;
import io.helidon.security.SecurityEnvironment;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

class CustomJWTAuthenticationTest {

    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlxfBorPoJuiDTnmih8daK+ERlniVxVPK+KMD3BbWf9CaoZkxTxcbz0ikxrGXUMKWscYm6l08lh7M8UioMfQpxSiI1dFeCLlKkcrkr45Gi1fcNFWxeTcVxoVDXLmW04QnREBQVrrrE64DKPWepHjZYhGUs1ziZvpJ4gPVBo5hAEHROnZrP/axlk5QCui2I+4U/DLkiXYQInsFY4xycPgJQjQnwypoDOk5BwiaUQlYACbSI9I5hjAnSS53AgdD5J5AerB0uD4uchmvipUngjmtgT5NWLMfwX8Um4Q7655YfrLIQB7UQVlbukX6UqlyeVBGB145YXqc+DQmzhfFb9TUbwIDAQAB";
    private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXF8Gis+gm6INOeaKHx1or4RGWeJXFU8r4owPcFtZ/0JqhmTFPFxvPSKTGsZdQwpaxxibqXTyWHszxSKgx9CnFKIjV0V4IuUqRyuSvjkaLV9w0VbF5NxXGhUNcuZbThCdEQFBWuusTrgMo9Z6keNliEZSzXOJm+kniA9UGjmEAQdE6dms/9rGWTlAK6LYj7hT8MuSJdhAiewVjjHJw+AlCNCfDKmgM6TkHCJpRCVgAJtIj0jmGMCdJLncCB0PknkB6sHS4Pi5yGa+KlSeCOa2BPk1Ysx/BfxSbhDvrnlh+sshAHtRBWVu6RfpSqXJ5UEYHXjlhepz4NCbOF8Vv1NRvAgMBAAECggEAC1AZDICh8j/YjQiodbgkdUKd2YFf3W1/tgwmntUK7HkHCvB4c66MQ4Hx7HqhlMdtFxR6F3QxtB/MQoipJ0xb0EpPrIrHmqmc/ruNUeO6PnauozCDYEsSk1GpEIlI1L+BXSffnA7UyR1vWSLC5RezVxdv5dofjYxR+d1Flv8UHjux5jFjgx1w/qmO79KdoWjxvk5nxs9fCufGuog27V67w9gicLRYTKOLqZa+0WqWq5WNsEDxateNcG/Y2CxGVNlEGo2yth3CCq6GrSZ6v2Ul/vEdxejWI1lIBTGhP2d1a9U+8CymI32FLgQPgBshKLktccYS0YoVIhj3mFMv8f9rYQKBgQDEx2jnZ41rXvJvxZrKqPlfCNYavf2niCnJamkTj6QhGTGfZQ//N+Lfrv7cuINIwviD4shwPjxu6J5z6V16WHw75f/FDsSc395kjzGERb0llZ1AU7g1tPOHBZinDD737H/fVQHo6goJ4oQM+o4Q8edGlodEktVMA7AHMZhibZh+XwKBgQDEkIOXihseCiaG1LEfzktOOQ3m/7zCbP2fz6pqbndgLJAOWXihBT0CAPHF70MEHiC6JwZBfiibNuponmv9w3RYYDeVvjbVoW+MhlzpuZiuuwAewYtRwQhgpGPCT6iU1KENrTVRlnQrUCd51I12t8EoMDnJyW8TfTJ+LlM8jK1D8QKBgCHCxLDPB2hghd+/W2thykcP0QwNhLN1nRW9eAGvJ022cNkUEcaL5BTUhU2BKqUVLmYSRqlbRCsTJfxlEXCoNchhEZzRh9ISZqmCF9DF1knYfX5H3tb4EMAdqa8fXr5SiS3b+9PmM4q2nklLw4wMtM1bjiGpv4anK1BQpgBdJUipAoGBAKhdfEml4tpVKXDd56VtpzUmS5EsRjtm7csgLkEhw6CeKvoPivKpMPtxYaX/7bAuOiwY1xPsAs4IOLpJ0Ag5jYNRTus1VXSg5ZJBWH5eGbi4tj6h2zq4k/trc4Nxhd2NTzIOxZfPvkuHSXjiJhWlCY7p1+XOYxUXbNOFzIgVmyeRAoGAbivQRcn2TIfHZDp2RuXgmYcLKeV4edCidL3FsqbNg/KAgWm5Ya7VXBqteoO76v1JKljgX29+Z+TJUKa2MO6xbDwqOu+uKiYxC66j7zKgIgMTv/oXlnOe0mE5VfwayAncI3AlWR0sYJUgW7WtGESk7jUR7HlWkbWl30FOrhIV0E8=";

    CustomJWTAuthentication subject;

    @BeforeEach
    public void setUp() {
        //Arrange
        SigningKeyResolver signingKeyResolver = Mockito.mock(SigningKeyResolver.class);
        Mockito.doReturn(getPublicKey()).when(signingKeyResolver).resolveSigningKey(Mockito.any(), (Claims) Mockito.any());

        subject = new CustomJWTAuthentication(signingKeyResolver);
        subject = Mockito.spy(subject);
    }

    @Test
    void syncAuthenticateValidHeaderTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256).compact();

        Map<String, List<String>> headers = new HashMap<>();
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer " + jws);
        headers.put("Authorization", authorization);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);
        //Assert
        Assertions.assertTrue(result.status().isSuccess());
    }

    @Test
    void syncAuthenticateNoJWTTest() {
        //Arrange
        Map<String, List<String>> headers = new HashMap<>();
        List<String> authorization = new ArrayList<>();
        headers.put("Authorization", authorization);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);

        //Assert
        Assertions.assertFalse(result.status().isSuccess());
        Assertions.assertEquals("No JWT Token found",result.description().get());
    }

    @Test
    void syncAuthenticateExpiredTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .setExpiration(new Date(0))
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256).compact();

        Map<String, List<String>> headers = new HashMap<>();
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer " + jws);
        headers.put("Authorization", authorization);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);
        //Assert
        Assertions.assertFalse(result.status().isSuccess());
        Assertions.assertEquals("Token has expired", result.description().get());
    }

    @Test
    void syncAuthenticateMalformedHeaderTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256).compact();

        Map<String, List<String>> headers = new HashMap<>();
        List<String> authorization = new ArrayList<>();
        authorization.add(jws);
        headers.put("Authorization", authorization);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);
        //Assert
        Assertions.assertFalse(result.status().isSuccess());
        Assertions.assertEquals("Not a valid token", result.description().get());
    }

    @Test
    void syncAuthenticateWrongSignatureTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(Keys.keyPairFor(SignatureAlgorithm.RS256).getPrivate(), SignatureAlgorithm.RS256).compact();

        Map<String, List<String>> headers = new HashMap<>();
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer " + jws);
        headers.put("Authorization", authorization);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);
        //Assert
        Assertions.assertFalse(result.status().isSuccess());
        Assertions.assertEquals("Token signature is invalid", result.description().get());
    }

    @Test
    void syncAuthenticateNoSignatureTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .compact();

        Map<String, List<String>> headers = new HashMap<>();
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer " + jws);
        headers.put("Authorization", authorization);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);
        //Assert
        Assertions.assertFalse(result.status().isSuccess());
        Assertions.assertEquals("Token signature is invalid", result.description().get());
    }

    @Test
    void syncAuthenticateValidCookieTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256).compact();

        Map<String, List<String>> headers = new HashMap<>();
        List<String> cookie = new ArrayList<>();
        cookie.add("JWT=" + jws);
        headers.put("Cookie", cookie);

        ProviderRequest providerRequest = Mockito.mock(ProviderRequest.class);
        SecurityContext sc = Mockito.mock(SecurityContext.class);
        Mockito.doReturn("This is a test").when(sc).id();
        Mockito.doReturn(sc).when(providerRequest).securityContext();
        SecurityEnvironment se = Mockito.mock(SecurityEnvironment.class);
        Mockito.doReturn(headers).when(se).headers();
        Mockito.doReturn(se).when(providerRequest).env();
        //Act

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);

        //Assert
        Assertions.assertTrue(result.status().isSuccess());
    }

    protected Key getPublicKey() {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot create public key");
        }
    }

    protected Key getPrivateKey() {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot create public key");
        }
    }
}
