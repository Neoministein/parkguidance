package com.neo.parkguidance.ms.user.impl.rest.entity;

import com.neo.parkguidance.common.api.json.Views;
import com.neo.parkguidance.ms.user.api.rest.RestAction;
import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.impl.rest.DefaultV1Response;
import com.neo.parkguidance.ms.user.impl.rest.HttpMethod;
import io.helidon.security.annotations.Authenticated;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.persistence.PersistenceException;
import javax.transaction.RollbackException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Path(UserResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class UserResource extends AbstractEntityRestEndpoint<RegisteredUser> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    public static final String RESOURCE_LOCATION = "/api/resource/user";

    @GET
    @Authenticated
    public Response getCurrentUser() {
        RestAction restAction = () -> {
            RegisteredUser currentUser = entityDao.find(UUID.fromString(requestDetails.getUUId()));
            if (currentUser == null) {
                LOGGER.error("User identifier [{}] does not match an database entity", requestDetails.getUUId()); // TODO THROUGH ALARM
                return DefaultV1Response.error(
                        400,
                        DefaultV1Response.errorObject(
                                "pgs/resources/000",
                                "Entity not found"),
                        getContext(HttpMethod.GET, ""));
            }
            LOGGER.trace("Entity lookup success [{},id:{}]", getEntityClass().getSimpleName(), currentUser.getPrimaryKey());
            return parseEntityToResponse(currentUser, "", Views.Owner.class);
        };
        return super.restCall(restAction, HttpMethod.GET, "");
    }

    @Override
    @PUT
    @Authenticated
    public Response edit(String x) {
        RestAction action = () -> {
            RegisteredUser entity = parseJSONToEntity(x, Views.Owner.class);

            if (!entity.getPrimaryKey().equals(requestDetails.getUUId()) && !
                    requestDetails.isInRole(ENTITY_PERM + getEntityClass().getSimpleName())) {
                LOGGER.warn("Invalid user permission to edit another user");
                return DefaultV1Response.error(4014, E_INVALID_RESOURCE_PERMISSION, getContext(HttpMethod.POST, ""));
            }

            try {
                entityDao.edit(entity);
                LOGGER.info("Created new [{},{}]",getEntityClass().getSimpleName(), entity.getPrimaryKey());
            } catch (RollbackException ex) {
                LOGGER.debug("Provided value isn't unique");
                return DefaultV1Response.error(400, E_NOT_UNIQUE, getContext(HttpMethod.POST, ""));
            } catch (PersistenceException ex) {
                LOGGER.debug("Entity is missing mandatory fields");
                return DefaultV1Response.error(400, E_MISSING_FIELDS, getContext(HttpMethod.POST, ""));
            }
            return parseEntityToResponse(entity, getContext(HttpMethod.POST, ""), Views.Owner.class);
        };
        return super.restCall(action, HttpMethod.POST, "", List.of(ENTITY_PERM + getEntityClass().getSimpleName()));
    }

    @GET
    @Path("/username/{username}")
    @Authenticated(optional = true)
    public Response getByUsername(@PathParam("username") String username, @DefaultValue("false") @QueryParam("relations") Boolean relations) {
        RestAction action = () -> entityByColumn(RegisteredUser.C_EMAIL, username, relations,getClassURI() + "/username/");
        return super.restCall(action, HttpMethod.GET, "/username/" + username, List.of(ENTITY_PERM + getEntityClass().getSimpleName()));
    }

    @GET
    @Path("/email/{email}")
    @Authenticated(optional = true)
    public Response getByEmail(@PathParam("email") String email, @DefaultValue("false") @QueryParam("relations") Boolean relations) {
        LOGGER.debug("");
        RestAction action = () -> entityByColumn(RegisteredUser.C_EMAIL, email, relations,getClassURI() + "/email/");
        return super.restCall(action, HttpMethod.GET, "/email/" + email,List.of(ENTITY_PERM + getEntityClass().getSimpleName()));
    }

    @Override
    protected String getClassURI() {
        return RESOURCE_LOCATION;
    }

    @Override
    protected Object convertToPrimaryKey(String primaryKey) {
        try {
            return UUID.fromString(primaryKey);
        } catch (IllegalArgumentException ex) {
            return new Object();
        }

    }

    @Override
    protected Class<RegisteredUser> getEntityClass() {
        return RegisteredUser.class;
    }
}
