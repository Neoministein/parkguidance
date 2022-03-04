package com.neo.parkguidance.ms.user.impl.rest;

import com.neo.parkguidance.ms.user.api.security.jwt.KeyService;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.impl.security.TokenType;
import io.helidon.security.SecurityContext;
import io.helidon.security.annotations.Authenticated;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@RequestScoped
@Path("/api/test")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class UserResource {

    @Inject
    EntityDao<UserToken> userTokenEntityDao;

    @Inject
    KeyService keyService;

    @GET
    @Authenticated(optional = true)
    public Response get(@Context SecurityContext securityContext) {
        userTokenEntityDao.create(new UserToken("", TokenType.REFRESH, new Date(),null));
        return Response.ok().entity("{ \"status\": 200}").build();
    }
}
