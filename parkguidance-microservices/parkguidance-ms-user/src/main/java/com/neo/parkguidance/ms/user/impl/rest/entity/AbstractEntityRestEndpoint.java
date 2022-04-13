package com.neo.parkguidance.ms.user.impl.rest.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.parkguidance.common.api.json.Views;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import com.neo.parkguidance.ms.user.api.rest.RestAction;
import com.neo.parkguidance.ms.user.impl.rest.AbstractRestEndpoint;
import com.neo.parkguidance.ms.user.impl.rest.DefaultV1Response;
import com.neo.parkguidance.ms.user.impl.rest.HttpMethod;
import io.helidon.security.annotations.Authenticated;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.RollbackException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public abstract class AbstractEntityRestEndpoint<T extends DataBaseEntity> extends AbstractRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityRestEndpoint.class);

    protected static final JSONObject E_NOT_FOUND = DefaultV1Response.errorObject("pgs/resources/000","Entity not found");
    protected static final JSONObject E_CANNOT_PARSE = DefaultV1Response.errorObject("pgs/resources/001","Unable to retrieve entity");
    protected static final JSONObject E_MISSING_FIELDS = DefaultV1Response.errorObject("pgs/resources/002","Entity is missing mandatory fields");
    protected static final JSONObject E_NOT_UNIQUE = DefaultV1Response.errorObject("pgs/resources/003","Provided value isn't unique");
    protected static final JSONObject E_INVALID_RESOURCE_PERMISSION = DefaultV1Response.errorObject("pgs/resources/004","Invalid resource permission");

    protected static final String ENTITY_PERM = "CRUD_";

    protected static final String P_GET_ID = "/id";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    protected EntityDao<T> entityDao;

    protected abstract Object convertToPrimaryKey(String primaryKey);

    protected abstract Class<T> getEntityClass();

    @GET
    @Path(P_GET_ID + "/{id}")
    @Authenticated(optional = true)
    public Response getByPrimaryKey(@PathParam("id") String primaryKey, @DefaultValue("false") @QueryParam("relations") Boolean relations) {
        RestAction action = () -> entityByColumn(DataBaseEntity.C_ID, convertToPrimaryKey(primaryKey), relations, "/id/");
        return super.restCall(action, HttpMethod.GET,P_GET_ID + "/" + primaryKey);
    }

    @POST
    @Authenticated(optional = true)
    public Response create(String x) {
        RestAction action = () -> {
            T entity = parseJSONToEntity(x, getSerializationScope(false));
            try {
                entityDao.create(entity);
                LOGGER.info("Created new [{},{}]",getEntityClass().getSimpleName(), entity.getPrimaryKey());
            } catch (RollbackException ex) {
                LOGGER.debug("Provided value isn't unique");
                return DefaultV1Response.error(400, E_NOT_UNIQUE, getContext(HttpMethod.POST, ""));
            } catch (PersistenceException ex) {
                LOGGER.debug("Entity is missing mandatory fields");
                return DefaultV1Response.error(400, E_MISSING_FIELDS, getContext(HttpMethod.POST, ""));
            }
            return parseEntityToResponse(entity, getContext(HttpMethod.POST, ""), getSerializationScope(false));
        };

        return super.restCall(action, HttpMethod.POST, "", List.of(ENTITY_PERM + getEntityClass().getSimpleName()));
    }

    @PUT
    @Authenticated
    public Response edit(String x) {
        RestAction action = () -> {
            T entity = parseJSONToEntity(x, getSerializationScope(false));
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
            return parseEntityToResponse(entity, getContext(HttpMethod.POST, ""), getSerializationScope(false));
        };
        return super.restCall(action, HttpMethod.POST, "", List.of(ENTITY_PERM + getEntityClass().getSimpleName()));
    }


    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response delete(@PathParam("id") String primaryKey) {
        RestAction action = () -> {
            T entity = entityDao.find(convertToPrimaryKey(primaryKey));

            if (entity == null) {
                LOGGER.debug("Entity not found [{},{}]", getEntityClass().getSimpleName() ,primaryKey);
                return DefaultV1Response.error(404, E_NOT_FOUND, getContext(HttpMethod.DELETE, "/" + primaryKey));
            }

            try {
                entityDao.remove(entity);
                LOGGER.info("Deleted entity [{},{}]",getEntityClass().getSimpleName(), entity.getPrimaryKey());
            } catch (RollbackException ex) {
                return DefaultV1Response.error(400, E_MISSING_FIELDS, getContext(HttpMethod.DELETE, "/" + primaryKey));

            } catch (Exception ex) {
                return DefaultV1Response.error(400, E_NOT_UNIQUE, getContext(HttpMethod.DELETE, "/" + primaryKey));
            }
            return DefaultV1Response.success(getContext(HttpMethod.DELETE, "/" + primaryKey));
        };

        return super.restCall(action, HttpMethod.DELETE, "/" + primaryKey, List.of(ENTITY_PERM + getEntityClass().getSimpleName()));
    }

    /**
     * Retrieves an entity by a column value association and generates a response
     *
     * @param field the entity field
     * @param value the value which entity field must have
     * @param relations if the entity should return relations
     * @param resourceLocation the resource location of the caller
     *
     * @return the response to be delivered to the client
     */
    protected Response entityByColumn(String field, Object value, boolean relations, String resourceLocation) {
        T entity = entityDao.findOneByColumn(field, value);
        if (entity == null) {
            LOGGER.debug("Entity not found [{},{}:{}]", getEntityClass().getSimpleName(), field, value);
            return DefaultV1Response.error(404, E_NOT_FOUND, getContext(HttpMethod.GET, resourceLocation + value));
        }
        LOGGER.trace("Entity lookup success [{},{}:{}]", getEntityClass().getSimpleName(), field, value);
        return parseEntityToResponse(entity,resourceLocation + value, getSerializationScope(relations));
    }

    /**
     * Parsed the entity to a JSON response
     *
     * @param entity the entity to parse
     * @param methodURI the method URI
     * @param serializationScope the jackson serialization scope
     *
     * @return the response to be delivered to the client
     */
    protected Response parseEntityToResponse(T entity, String methodURI, Class<?> serializationScope) {
        try {
            String result = new ObjectMapper()
                    .writerWithView(serializationScope)
                    .writeValueAsString(entity);
            return DefaultV1Response
                    .success(getContext(HttpMethod.GET, methodURI), new JSONArray().put(new JSONObject(new JSONTokener(result))));
        } catch (JSONException | JsonProcessingException ex) {
            LOGGER.error("Unable to parse database entity to JSON {}", ex.getMessage());
            return DefaultV1Response.error(500, E_CANNOT_PARSE, getContext(HttpMethod.GET, methodURI));
        }
    }

    protected Class<?> getSerializationScope(boolean relations) {
        Class<?> serializationScope = Views.Public.class;

        if (requestDetails.getUUId() != null) {
            if (requestDetails.isInRole(ENTITY_PERM + getEntityClass().getSimpleName())) {
                serializationScope = Views.Owner.class;
            }

            if (requestDetails.isInRole("internal")) {
                serializationScope = Views.Internal.class;
            }
        }
        return Views.retrieveRelations(serializationScope, relations);
    }

    protected T parseJSONToEntity(String x, Class<?> serializationScope) {
        try {
            return objectMapper.readerWithView(serializationScope).readValue(x, getEntityClass());
        } catch (IOException ex) {
            throw new JSONException(ex);
        }
    }

    public void setEntityDao(EntityDao<T> entityDao) {
        this.entityDao = entityDao;
    }
}
