package com.neo.parkguidance.ms.user.impl.rest.key;

import com.neo.parkguidance.ms.user.impl.AbstractIntegrationTest;
import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.tests.junit5.AddExtension;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;

@HelidonTest
@AddExtension(ConfigCdiExtension.class)
class KeyResourceIT extends AbstractIntegrationTest {

    @Inject
    WebTarget webTarget;

    @BeforeEach
    void revokeKeyBeforeTest() {
        String jwt = getValidJWTToken(List.of("revokeActiveKeys"));
        webTarget.path(KeyResource.RESOURCE_LOCATION + KeyResource.REVOKE_ACTIVE_KEYS).request()
                .header("Authorization", "Bearer " + jwt)
                .method("POST");
    }

    @Test
    void revokeActiveKeyTest() {
        //Arrange
        String jwt = getValidJWTToken(List.of("revokeActiveKeys"));

        //Act
        Response responseOne = webTarget.path(KeyResource.RESOURCE_LOCATION + KeyResource.ACTIVE_PUBLIC_KEYS).request()
                .method("GET");

        Response responseTwo = webTarget.path(KeyResource.RESOURCE_LOCATION + KeyResource.REVOKE_ACTIVE_KEYS).request()
                .header("Authorization", "Bearer " + jwt)
                .method("POST");

        Response responseThree = webTarget.path(KeyResource.RESOURCE_LOCATION + KeyResource.ACTIVE_PUBLIC_KEYS).request()
                .method("GET");

        //Assert
        JSONObject beforeRevoke = validateResponse(responseOne);
        validateResponse(responseTwo);
        JSONObject afterRevoke = validateResponse(responseThree);

        String beforeId = beforeRevoke.getJSONArray("data").getJSONObject(0).getString("kid");
        String afterId = afterRevoke.getJSONArray("data").getJSONObject(0).getString("kid");

        Assertions.assertNotEquals(beforeId,afterId);
    }

    @Test
    void reliveCurrentKey() {
        //Arrange
        String jwt = getValidJWTToken(List.of("reliveAccessKey"));

        //Act
        Response responseOne = webTarget.path(KeyResource.RESOURCE_LOCATION + KeyResource.ACTIVE_PUBLIC_KEYS).request()
                .method("GET");

        Response responseTwo = webTarget.path(KeyResource.RESOURCE_LOCATION).request()
                .header("Authorization", "Bearer " + jwt)
                .method("POST");

        Response responseThree = webTarget.path(KeyResource.RESOURCE_LOCATION + KeyResource.ACTIVE_PUBLIC_KEYS).request()
                .method("GET");

        //Assert
        JSONObject beforeRevoke = validateResponse(responseOne);
        validateResponse(responseTwo);
        JSONObject afterRevoke = validateResponse(responseThree);

        Assertions.assertEquals(1,beforeRevoke.getJSONArray("data").length());
        Assertions.assertEquals(2,afterRevoke.getJSONArray("data").length());
    }
}
