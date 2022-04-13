package com.neo.parkguidance.ms.user.impl.rest.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import com.neo.parkguidance.ms.user.impl.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class AbstractEntityRestEndpointIT<T extends DataBaseEntity> extends AbstractIntegrationTest {

    @Inject
    WebTarget webTarget;

    protected abstract Class<?> entityClass();

    protected abstract JSONObject defaultJSONEntity();

    protected abstract JSONObject editedJSONEntity();

    protected abstract String resourceLocation();

    @Test
    @Order(0)
    void createEntityTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));

        Entity<String> content = Entity.entity(defaultJSONEntity().toString(), MediaType.APPLICATION_JSON_TYPE);
        //Act
        Response response = webTarget.path(resourceLocation()).request()
                .header("Authorization", "Bearer " + jwt)
                .method("POST", content);

        //Assert
        validateResponse(response,200);
    }

    @Test
    @Order(1)
    void createExistingEntityTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));
        Entity<String> content = Entity.entity(defaultJSONEntity().toString(), MediaType.APPLICATION_JSON_TYPE);
        //Act
        Response response = webTarget.path(resourceLocation()).request()
                .header("Authorization", "Bearer " + jwt)
                .method("POST", content);

        //Assert
        JSONObject responseBody = validateResponse(response,400);
        Assertions.assertEquals("pgs/resources/003", responseBody.getJSONObject("error").getString("error"));
    }

    @Test
    @Order(2)
    void retrieveEntityByIdTest() {
        //Arrange

        //Act
        Response response = webTarget.path(resourceLocation() + AbstractEntityRestEndpoint.P_GET_ID + "/" + defaultJSONEntity().getString("id")).request()
                .method("GET");

        //Assert
        JSONObject responseBody = validateResponse(response,200);
        JSONObject responseEntity = responseBody.getJSONArray("data").getJSONObject(0);

        Assertions.assertEquals(defaultJSONEntity().getString("id"), responseEntity.getString("id"));
    }

    @Test
    @Order(3)
    void editEntityTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));

        Entity<String> content = Entity.entity(defaultJSONEntity().toString(), MediaType.APPLICATION_JSON_TYPE);
        //Act
        Response response = webTarget.path(resourceLocation()).request()
                .header("Authorization", "Bearer " + jwt)
                .method("PUT", content);

        //Assert
        JSONObject responseBody = validateResponse(response,200);

        JSONObject responseEntity = responseBody.getJSONArray("data").getJSONObject(0);

        Assertions.assertEquals(defaultJSONEntity().getString("id"), responseEntity.getString("id"));
    }

    @Test
    @Order(4)
    void deleteEntityTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));

        //Act
        Response response = webTarget.path(resourceLocation() + "/" + defaultJSONEntity().getString("id")).request()
                .header("Authorization", "Bearer " + jwt)
                .method("DELETE");

        //Assert
        validateResponse(response,200);
    }

    @Test
    void createMissingFieldEntityTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));
        JSONObject entity = new JSONObject();

        Entity<String> content = Entity.entity(entity.toString(), MediaType.APPLICATION_JSON_TYPE);
        //Act
        Response response = webTarget.path(resourceLocation()).request()
                .header("Authorization", "Bearer " + jwt)
                .method("POST", content);

        //Assert
        JSONObject responseBody = validateResponse(response,400);
        Assertions.assertEquals("pgs/resources/002", responseBody.getJSONObject("error").getString("error"));
    }

    @Test
    void deleteNonexistentEntityTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));

        //Act
        Response response = webTarget.path(resourceLocation() + "/" + "NonexistentEntity").request()
                .header("Authorization", "Bearer " + jwt)
                .method("DELETE");

        //Assert
        validateResponse(response,404);
    }

    @Test
    void missingCrudPermissionTest() {
        //Act
        Response edit  = webTarget.path(resourceLocation()).request().method("PUT", Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE));
        Response delete  = webTarget.path(resourceLocation() + "/" + "ENTITY").request().method("DELETE");

        //Assert
        Assertions.assertEquals(401, edit.getStatus());
        Assertions.assertEquals(401, delete.getStatus());
    }

    @Test
    void retrieveNoneExistentEntityByIdTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of(AbstractEntityRestEndpoint.ENTITY_PERM + entityClass().getSimpleName()));

        //Act
        Response response = webTarget.path(resourceLocation() + AbstractEntityRestEndpoint.P_GET_ID + "/" + "ENTITY").request()
                .header("Authorization", "Bearer " + jwt)
                .method("GET");

        //Assert
        validateResponse(response,404);
    }
}
