package com.neo.parkguidance.ms.security.impl.authentication;

import com.neo.parkguidance.common.impl.util.KeyUtils;
import io.helidon.config.Config;
import io.helidon.config.ConfigValue;
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

import java.util.*;

class CustomJWTAuthenticationTest extends AbstractJWTTest {

    CustomJWTAuthentication subject;

    @BeforeEach
    public void setUp() {
        //Arrange
        SigningKeyResolver signingKeyResolver = Mockito.mock(SigningKeyResolver.class);
        Mockito.doReturn(KeyUtils.parseRSAPublicKey(getPublicKey())).when(signingKeyResolver).resolveSigningKey(Mockito.any(), (Claims) Mockito.any());

        subject = new CustomJWTAuthentication(signingKeyResolver);
        subject = Mockito.spy(subject);
    }

    @Test
    void integrationTestProviderTest() {
        //Arrange
        Config config = Mockito.mock(Config.class);
        Config configContainer = Mockito.mock(Config.class);
        ConfigValue<Boolean> configValue = Mockito.mock(ConfigValue.class);

        Mockito.doReturn(configContainer).when(config).get(CustomJWTAuthentication.IS_INTEGRATION_TEST);
        Mockito.doReturn(configValue).when(configContainer).asBoolean();
        Mockito.doReturn(true).when(configValue).orElse(false);

        subject = new CustomJWTAuthentication(config);
        subject = Mockito.spy(subject);

        //Act
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(KeyUtils.parseRSAPrivateKey(getPrivateKey()), SignatureAlgorithm.RS256).compact();

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

        AuthenticationResponse result = subject.syncAuthenticate(providerRequest);
        //Assert
        Assertions.assertTrue(result.status().isSuccess());

    }

    @Test
    void syncAuthenticateValidHeaderTest() {
        //Arrange
        String jws = Jwts.builder()
                .setSubject("uuid")
                .claim("username", "Testing-User")
                .claim("roles", new ArrayList<>())
                .signWith(KeyUtils.parseRSAPrivateKey(getPrivateKey()), SignatureAlgorithm.RS256).compact();

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
                .signWith(KeyUtils.parseRSAPrivateKey(getPrivateKey()), SignatureAlgorithm.RS256).compact();

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
                .signWith(KeyUtils.parseRSAPrivateKey(getPrivateKey()), SignatureAlgorithm.RS256).compact();

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
                .signWith(KeyUtils.parseRSAPrivateKey(getPrivateKey()), SignatureAlgorithm.RS256).compact();

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
}
