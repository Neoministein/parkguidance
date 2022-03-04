package com.neo.parkguidance.ms.user.impl.rest.key;

import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;
import com.neo.parkguidance.ms.user.api.rest.RestAction;
import com.neo.parkguidance.ms.user.api.security.jwt.KeyService;
import com.neo.parkguidance.ms.user.impl.rest.AbstractRestEndpoint;
import com.neo.parkguidance.ms.user.impl.rest.DefaultV1Response;
import com.neo.parkguidance.ms.user.impl.rest.HttpMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;

@RequestScoped
@Path(KeyResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class KeyResource extends AbstractRestEndpoint {

    public static final String RESOURCE_LOCATION = "/api/key";

    private static final String ACTIVE_PUBLIC_KEYS = "/activePublicKeys";

    @Inject
    KeyService keyService;

    @GET
    @Path(ACTIVE_PUBLIC_KEYS)
    public Response activePublicKey() {
        RestAction restAction = () -> {
            JSONArray keys = new JSONArray();

            for (JWTPublicKey jwtPublicKey: keyService.getActivePublicKeys()) {
                JSONObject key = new JSONObject();
                key.put("kid", jwtPublicKey.getId());
                key.put("exp", jwtPublicKey.getExpirationDate().getTime());
                key.put("key", Base64.getEncoder().encodeToString(jwtPublicKey.getKey().getEncoded()));
                keys.put(key);
            }

            return DefaultV1Response.success(getContext(HttpMethod.GET, ACTIVE_PUBLIC_KEYS), keys);
        };

        return super.restCall(restAction, HttpMethod.GET, ACTIVE_PUBLIC_KEYS);
    }



    @Override
    protected String getClassURI() {
        return RESOURCE_LOCATION;
    }
}
