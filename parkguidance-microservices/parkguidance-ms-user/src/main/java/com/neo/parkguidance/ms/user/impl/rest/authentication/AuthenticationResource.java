package com.neo.parkguidance.ms.user.impl.rest.authentication;

import com.neo.parkguidance.ms.user.impl.rest.AbstractRestEndpoint;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/api/authenticate")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class AuthenticationResource extends AbstractRestEndpoint {

    @GET
    public Response get() {
        return null;
    }

    @Override
    protected String getContext(String method) {
        return null;
    }
}
