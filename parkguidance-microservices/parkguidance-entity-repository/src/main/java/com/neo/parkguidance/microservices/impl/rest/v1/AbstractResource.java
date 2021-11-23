package com.neo.parkguidance.microservices.impl.rest.v1;

import com.neo.parkguidance.microservices.api.v1.RestAction;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public abstract class AbstractResource {

    public Response restCall(RestAction restAction) {
        try {
            return restAction.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.ok().entity(DefaultV1Response.error(ex, getContext()).toString()).build();
        }
    }

    protected abstract String getContext();
}
