package com.neo.parkguidance.ms.user.impl.rest.entity;

import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.tests.junit5.AddExtension;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.UUID;

@HelidonTest
@AddExtension(ConfigCdiExtension.class)
class UserResourceIT extends AbstractEntityRestEndpointIT<RegisteredUser> {

    @Inject
    WebTarget webTarget;

    @Test
    void currentUserTest() {
        String jwt = getValidJWTToken();

        Response response = webTarget.path(UserResource.RESOURCE_LOCATION).request()
                .header("Authorization", "Bearer " + jwt)
                .method("GET");


        JSONObject responseBody = validateResponse(response,200);
        JSONObject responseEntity = responseBody.getJSONArray("data").getJSONObject(0);

        Assertions.assertEquals("9cf9de30-9004-11ec-b909-0242ac120002",responseEntity.getString("id"));
        Assertions.assertEquals("defaultUser",responseEntity.getString("username"));
        Assertions.assertEquals("email@email.com",responseEntity.getString("email"));
        Assertions.assertTrue(responseEntity.getBoolean("emailVerified"));
    }

    @Test
    void editCurrentUserTest() {
        String jwt = getValidJWTToken();

        Response response = webTarget.path(UserResource.RESOURCE_LOCATION).request()
                .header("Authorization", "Bearer " + jwt)
                .method("GET");


        JSONObject responseBody = validateResponse(response,200);
        JSONObject responseEntity = responseBody.getJSONArray("data").getJSONObject(0);

        Assertions.assertEquals("9cf9de30-9004-11ec-b909-0242ac120002",responseEntity.getString("id"));
        Assertions.assertEquals("defaultUser",responseEntity.getString("username"));
        Assertions.assertEquals("email@email.com",responseEntity.getString("email"));
        Assertions.assertTrue(responseEntity.getBoolean("emailVerified"));
    }

    @Override
    protected Class<?> entityClass() {
        return RegisteredUser.class;
    }

    @Override
    protected JSONObject defaultJSONEntity() {
        JSONObject entity = new JSONObject();
        entity.put("id", "1a61d055-18fb-42fd-b6fb-73a3909f6244");
        entity.put("username","IntegrationTestUser");
        entity.put("email","ItUser@email.com");
        return entity;
    }

    @Override
    protected JSONObject editedJSONEntity() {
        JSONObject entity = new JSONObject();
        entity.put("id", "1a61d055-18fb-42fd-b6fb-73a3909f6244");
        entity.put("username","IntegrationTestUser");
        entity.put("email","ItUser@email.com");
        return entity;
    }

    @Override
    protected String resourceLocation() {
        return UserResource.RESOURCE_LOCATION;
    }
}
