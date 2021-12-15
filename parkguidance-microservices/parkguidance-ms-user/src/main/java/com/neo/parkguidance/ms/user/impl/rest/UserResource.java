package com.neo.parkguidance.ms.user.impl.rest;

import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import io.helidon.security.SecurityContext;
import io.helidon.security.annotations.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/api/test")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class UserResource {

    private static final Logger LOGGER =  LoggerFactory.getLogger(UserResource.class);


    @Inject
    EntityDao<RegisteredUser> registeredUserDao;

    @GET
    @Authenticated(optional = true)
    public Response get(@Context SecurityContext securityContext) {
        MDC.put("traceId", securityContext.id());
        LOGGER.info("TEST A");
        securityContext.isUserInRole("");
        registeredUserDao.findAll();
        return Response.ok().entity("{ \"status\": 200}").build();
    }
}
