package com.neo.parkguidance.microservices.impl.rest.v1;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.impl.utils.StringUtils;
import com.neo.parkguidance.framework.impl.validation.EntityValueValidationException;
import com.neo.parkguidance.microservices.api.v1.RestAction;
import com.neo.parkguidance.microservices.impl.validation.EntityValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

//TODO CHECK FOR JS INJECTION OR SQL INJECTION
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public abstract class AbstractEntityResource<T extends DataBaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityResource.class);

    private static final String CREATE = "/create";
    private static final String EDIT = "/edit";
    private static final String REMOVE = "/remove";
    private static final String COUNT = "/count";
    private static final String FIND = "/find";
    private static final String FIND_ALL = "/findAll";
    private static final String FIND_ALL_SORTED = "/findAllSorted";
    private static final String FIND_BY_VALUE = "/findByValue";
    private static final String FIND_ONE_BY_VALUE = "/findOneByValue";
    private static final String FIND_ONE_BY_VALUES = "/findOneByValues";
    private static final String FIND_LIKE_EXAMPLE = "/findLikeExample";
    private static final String FIND_LIKE_EXAMPLE_LAZY = "/findLikeExampleLazy";
    private static final String FIND_LIKE_EXAMPLE_COUNT = "/findLikeExampleCount";

    @POST
    @Path(CREATE)
    public Response create(String s) {
        RestAction restAction = () -> {
            T entity = getJSONEntityWrapper().toValidEntity(new JSONObject(new JSONTokener(s)), getCallerPermissions());
            if (entity == null) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "Missing attributes to create an entity"),
                        getContext(CREATE));
            }
            getEntityDao().create(entity);
            return DefaultV1Response.success(
                    createNullableJSONArray(getJSONEntityWrapper().toJSON(entity,getCallerPermissions())), getContext(CREATE));
        };
        return restCall(restAction, CREATE);
    }

    @POST
    @Path(EDIT)
    public Response edit(String s) {
        RestAction restAction = () -> {
            try {
                T entity = getJSONEntityWrapper().toValidEntity(new JSONObject(new JSONTokener(s)), getCallerPermissions());
                getEntityDao().edit(entity);
            } catch (JSONException | EntityValidationException ex) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "Invalid json format in the request body"),
                        getContext(EDIT));
            }
            return DefaultV1Response.success(getContext(EDIT));
        };
        return restCall(restAction, EDIT);
    }

    @DELETE
    @Path(REMOVE)
    public Response remove(String s) {
        RestAction restAction = () -> {
            T entity = getJSONEntityWrapper().toValidEntity(new JSONObject(new JSONTokener(s)), getCallerPermissions());
            getEntityDao().remove(entity);
            return DefaultV1Response.success(getContext(REMOVE));
        };
        return restCall(restAction, REMOVE);
    }

    @GET
    @Path(COUNT)
    public Response count() {
        RestAction restAction = () -> {
            JSONObject result = new JSONObject();
            result.put("count", getEntityDao().count());
            return DefaultV1Response.success(createNullableJSONArray(result), getContext(COUNT));
        };

        return restCall(restAction, FIND);
    }

    @GET
    @Path(FIND)
    public Response find(@QueryParam("primaryKey") String primaryKey) {
        RestAction restAction = () -> {
            if (StringUtils.isEmpty(primaryKey)) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "The url parameter 'primaryKey' cannot be empty"),
                        getContext(FIND));
            }
            return DefaultV1Response.success(createNullableJSONArray(getJSONEntityWrapper().toJSON(
                    getEntityDao().find(primaryKey), getCallerPermissions())), getContext(FIND));
        };

        return restCall(restAction, FIND);
    }

    @GET
    @Path(FIND_ALL)
    public Response findAll() {
        RestAction restAction = () -> {
            List<T> allEntities = getEntityDao().findAll();

            JSONArray data = new JSONArray();
            for (T entity: allEntities) {
                addNullableValue(data, getJSONEntityWrapper().toJSON(entity, getCallerPermissions()));
            }

            return DefaultV1Response.success(data,getContext(FIND_ALL));
        };

        return restCall(restAction, FIND_ALL);
    }

    @GET
    @Path(FIND_ALL_SORTED)
    public Response findAll(@QueryParam("key") String key, @QueryParam("asc") Boolean asc) {
        RestAction restAction = () -> {
            if (StringUtils.isEmpty(key) || asc == null) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "The url parameter 'key' or 'asc' cannot be empty"),
                        getContext(FIND_ALL_SORTED));
            }

            List<T> allEntities = getEntityDao().findAll(key, asc);
            return DefaultV1Response.success(arrayFromList(allEntities, getCallerPermissions()),
                    getContext(FIND_ALL_SORTED));
        };

        return restCall(restAction, FIND_ALL_SORTED);
    }

    @GET
    @Path(FIND_ONE_BY_VALUE)
    public Response findOneByValue(@QueryParam("key") String key, @QueryParam("value") String value) {
        RestAction restAction = () -> {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "The url parameter 'key' or 'value' cannot be empty"),
                        getContext(FIND_ONE_BY_VALUE));
            }
            return DefaultV1Response.success(createNullableJSONArray(getJSONEntityWrapper().toJSON(
                    getEntityDao().findOneByColumn(key, value), getCallerPermissions())),getContext(FIND_ONE_BY_VALUE));
        };

        return restCall(restAction, FIND_ONE_BY_VALUE);
    }

    @GET
    @Path(FIND_BY_VALUE)
    public Response findByValue(@QueryParam("key") String key, @QueryParam("value") String value) {
        RestAction restAction = () -> {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "The url parameter 'key' or 'value' cannot be empty"),
                        getContext(FIND_BY_VALUE));
            }

            List<T> allEntities = getEntityDao().findByColumn(key, value);
            return DefaultV1Response.success(arrayFromList(allEntities, getCallerPermissions()),getContext(FIND_BY_VALUE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }


    @GET
    @Path(FIND_ONE_BY_VALUES)
    public Response findByValues(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody = new JSONObject(new JSONTokener(x));
            Map<String, Object> values = new HashMap<>();
            for (String key : jsonBody.keySet()) {
                values.put(key, jsonBody.get(key));
            }
            return DefaultV1Response.success(createNullableJSONArray(getJSONEntityWrapper().toJSON(
                    getEntityDao().findOneByColumn(values), getCallerPermissions())),getContext(FIND_ONE_BY_VALUES));
        };

        return restCall(restAction, FIND_ONE_BY_VALUES);
    }

    @GET
    @Path(FIND_LIKE_EXAMPLE)
    public Response findLikeExample(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody = new JSONObject(new JSONTokener(x));
            List<T> entity = getEntityDao().findLikeExample(getJSONEntityWrapper().toOptEntity(jsonBody.getJSONObject("entity")));
            return DefaultV1Response.success(arrayFromList(entity, getCallerPermissions()),getContext(FIND_LIKE_EXAMPLE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }

    @GET
    @Path(FIND_LIKE_EXAMPLE_LAZY)
    public Response findLikeExampleLazy(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody = new JSONObject(new JSONTokener(x));
            List<T> entityList = getEntityDao().findLikeExample(
                    getJSONEntityWrapper().toOptEntity(jsonBody.getJSONObject("entity")),
                    jsonBody.getInt("first"),
                    jsonBody.getInt("pageSize"),
                    jsonBody.getString("key"),
                    jsonBody.getBoolean("asc"));
            return DefaultV1Response.success(arrayFromList(entityList, getCallerPermissions()),getContext(FIND_LIKE_EXAMPLE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }

    @GET
    @Path(FIND_LIKE_EXAMPLE_COUNT)
    public Response findLikeExampleCount(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody = new JSONObject(new JSONTokener(x));
            Long count = getEntityDao().findCountLikeExample(getJSONEntityWrapper().toOptEntity(jsonBody.getJSONObject("entity")));
            return DefaultV1Response.success(createNullableJSONArray(count),getContext(FIND_LIKE_EXAMPLE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }

    protected JSONArray arrayFromList(List<T> entityList, Collection<String> callerPermissions) {
        JSONArray data = new JSONArray();
        for (T entity: entityList) {
            addNullableValue(data, getJSONEntityWrapper().toJSON(entity, callerPermissions));
        }
        return data;
    }

    public Response restCall(RestAction restAction, String method) {
        try {
            return restAction.run();
        } catch (EntityValueValidationException ex) {
            return DefaultV1Response.error(DefaultV1Response.errorObject(
                    400,
                    "Invalid value '"+ ex.getValue()+"' " + ex.getMessage()),
                    getContext(method));
        } catch (EntityValidationException ex) {
            return DefaultV1Response.error(DefaultV1Response.errorObject(
                    400,
                    "Invalid entity: " + ex.getMessage()),
                    getContext(method));
        } catch (JSONException ex) {
            return DefaultV1Response.error(DefaultV1Response.errorObject(
                    400,
                    "Invalid json format in the request body"),
                    getContext(method));
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("A unexpected exception occurred during a rest call", ex);
            return DefaultV1Response.error(
                    "Internal server error please try again later",
                    getContext(method));
        }
    }

    protected Collection<String> getCallerPermissions() {
        return Collections.emptySet();
    }

    protected void addNullableValue(JSONArray jsonArray, Object value) {
        if (value != null) {
            jsonArray.put(value);
        }
    }

    protected JSONArray createNullableJSONArray(Object value) {
        JSONArray jsonArray = new JSONArray();
        if (value == null) {
            return new JSONArray();
        }
        return jsonArray.put(value);
    }

    protected abstract JSONEntityWrapper<T> getJSONEntityWrapper();

    protected abstract EntityDao<T> getEntityDao();

    protected abstract String getContext(String method);
}
