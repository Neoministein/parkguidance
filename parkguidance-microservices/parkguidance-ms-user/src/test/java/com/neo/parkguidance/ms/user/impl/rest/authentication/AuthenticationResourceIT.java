package com.neo.parkguidance.ms.user.impl.rest.authentication;

import com.neo.parkguidance.ms.user.impl.AbstractIntegrationTest;
import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.tests.junit5.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@HelidonTest
@AddExtension(ConfigCdiExtension.class)
class AuthenticationResourceIT extends AbstractIntegrationTest {

    @Inject
    WebTarget webTarget;

    @Test
    void invalidJSONTest() {
        Entity<String> content = Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION + AuthenticationResource.P_CREDENTIALS).request().method("POST", content);


        Assertions.assertEquals(200, response.getStatus());
        JSONObject responseBody = new JSONObject(new JSONTokener(response.readEntity(String.class)));
        JSONObject errorObject = responseBody.getJSONObject("error");

        Assertions.assertEquals(400 ,responseBody.getInt("status"));
        Assertions.assertEquals("pgs/json/001", errorObject.getString("error"));
    }

    @Test
    void invalidPasswordTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("identification","defaultUser");
        requestBody.put("password","no");
        Entity<String> content = Entity.entity(requestBody.toString(), MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION + AuthenticationResource.P_CREDENTIALS).request().method("POST", content);

        JSONObject responseBody = validateResponse(response,401);
        JSONObject errorObject = responseBody.getJSONObject("error");

        Assertions.assertEquals("pgs/auth/001", errorObject.getString("error"));
    }

    @Test
    void defaultUserTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("identification","defaultUser");
        requestBody.put("password","password");
        Entity<String> content = Entity.entity(requestBody.toString(), MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION + AuthenticationResource.P_CREDENTIALS).request().method("POST", content);

        JSONObject responseBody = validateResponse(response);
        JSONArray data = responseBody.getJSONArray("data");
        Assertions.assertNotNull(data.getJSONObject(0).getString("jwt"));
    }

    @Test
    void defaultUserTokenTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("token", "jF3P4MC"); //TOKEN DEFINED IN SQL/data.sql
        Entity<String> content = Entity.entity(requestBody.toString(), MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION + AuthenticationResource.P_TOKEN).request().method("POST", content);


        JSONObject responseBody = validateResponse(response);
        JSONArray data = responseBody.getJSONArray("data");
        Assertions.assertNotNull(data.getJSONObject(0).getString("jwt"));
    }

    @Test
    void invalidUserTokenTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("token", "invalidToken");
        Entity<String> content = Entity.entity(requestBody.toString(), MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION + AuthenticationResource.P_TOKEN).request().method("POST", content);


        JSONObject responseBody = validateResponse(response,401);
        JSONObject errorObject = responseBody.getJSONObject("error");

        Assertions.assertEquals(401, responseBody.getInt("status"));
        Assertions.assertEquals("pgs/auth/000", errorObject.getString("error"));
    }

    @Test
    void validHeaderJWTTest() {
        String jwt = getValidJWTToken();

        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION).request()
                .header("Authorization", "Bearer " + jwt)
                .method("GET");

       validateResponse(response);
    }

    @Test
    void validCookieJWTTest() {
        String jwt = getValidJWTToken();

        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION).request()
                .cookie("JWT", jwt)
                .method("GET");

        validateResponse(response);
    }

    @Test
    void inValidHeaderJWTTest() {
        Response response = webTarget.path(AuthenticationResource.RESOURCE_LOCATION).request()
                .header("Authorization", "jwt")
                .method("GET");

        validateResponse(response,401);
    }
}
