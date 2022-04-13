package com.neo.parkguidance.ms.user.impl;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.common.impl.util.KeyUtils;
import com.neo.parkguidance.ms.user.impl.dao.*;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.List;

@AddBean( value = KeyPairTestRepository.class, scope = RequestScoped.class)
@AddBean( value = LoginAttemptTestRepository.class, scope = RequestScoped.class)
@AddBean( value = PermissionTestRepository.class, scope = RequestScoped.class)
@AddBean( value = RegisteredUserTestRepository.class, scope = RequestScoped.class)
@AddBean( value = RoleTestRepository.class, scope = RequestScoped.class)
@AddBean( value = UserCredentialsTestRepository.class, scope = RequestScoped.class)
@AddBean( value = UserTokenTestRepository.class, scope = RequestScoped.class)
public abstract class AbstractIntegrationTest {

    private static PrivateKey privateKey = null;

   @BeforeAll
    static void getPrivateKey() {
        try {
            StringBuilder sb = new StringBuilder();

            ClassLoader classLoader = MethodHandles.lookup().lookupClass().getClassLoader();


            InputStream inputStream = classLoader.getResourceAsStream("jwt-keys.json");
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null;) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(sb.toString()));
            privateKey = KeyUtils.parseRSAPrivateKey(jsonObject.getString("private"));
        } catch (NullPointerException | IOException | JSONException ex) {
            throw new InternalLogicException("Unable to retrieve private key from resources");
        }
    }

    protected String getValidJWTToken(List<String> roles) {
        return Jwts.builder()
                .setSubject("9cf9de30-9004-11ec-b909-0242ac120002")
                .claim("username", "Testing-User")
                .claim("roles", roles)
                .signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    protected String getValidJWTToken() {
        return getValidJWTToken(List.of());
    }

    protected JSONObject validateResponse(Response response) {
        return validateResponse(response, 200);
    }

    protected JSONObject validateResponse(Response response, int code) {
        Assertions.assertEquals(200, response.getStatus());
        JSONObject responseBody = new JSONObject(new JSONTokener(response.readEntity(String.class)));
        Assertions.assertEquals(code, responseBody.getInt("status"));
        return responseBody;
    }
}
