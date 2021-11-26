package com.neo.parkguidance.microservices.impl.rest.v1;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.impl.utils.StringUtils;
import com.neo.parkguidance.microservices.api.v1.RestAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public abstract class AbstractEntityResource<T extends DataBaseEntity> {

    private static final String FIND = "/find";
    private static final String FIND_ALL = "/findAll";
    private static final String FIND_ALL_SORTED = "/findAllSorted";
    private static final String FIND_BY_VALUE = "/findByValue";
    private static final String FIND_ONE_BY_VALUE = "/findOneByValue";
    private static final String FIND_ONE_BY_VALUES = "/findOneByValues";
    private static final String FIND_LIKE_EXAMPLE = "/findLikeExample";
    private static final String FIND_LIKE_EXAMPLE_LAZY = "/findLikeExampleLazy";
    private static final String FIND_LIKE_EXAMPLE_COUNT = "/findLikeExampleCount";

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
                    getEntityDao().find(primaryKey),true)), getContext(FIND));
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
                addNullableValue(data, getJSONEntityWrapper().toJSON(entity, true));
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
            return DefaultV1Response.success(arrayFromList(allEntities, true),getContext(FIND_ALL_SORTED));
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
                    getEntityDao().findOneByColumn(key, value), true)),getContext(FIND_ONE_BY_VALUE));
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
            return DefaultV1Response.success(arrayFromList(allEntities, true),getContext(FIND_BY_VALUE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }


    @GET
    @Path(FIND_ONE_BY_VALUES)
    public Response findByValues(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody;
            try {
                jsonBody = new JSONObject(new JSONTokener(x));
            } catch (JSONException ex) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "Invalid json format in the request body"),
                        getContext(FIND_BY_VALUE));
            }
            Map<String, Object> values = new HashMap<>();
            for (String key : jsonBody.keySet()) {
                values.put(key, jsonBody.get(key));
            }
            return DefaultV1Response.success(createNullableJSONArray(getJSONEntityWrapper().toJSON(
                    getEntityDao().findOneByColumn(values), true)),getContext(FIND_ONE_BY_VALUES));
        };

        return restCall(restAction, FIND_ONE_BY_VALUES);
    }

    @GET
    @Path(FIND_LIKE_EXAMPLE)
    public Response findLikeExample(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody;
            try {
                jsonBody = new JSONObject(new JSONTokener(x));
            } catch (JSONException ex) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "Invalid json format in the request body"),
                        getContext(FIND_BY_VALUE));
            }
            List<T> entity = getEntityDao().findLikeExample(getJSONEntityWrapper().toEntity(jsonBody.getJSONObject("entity")));

            return DefaultV1Response.success(arrayFromList(entity,true),getContext(FIND_LIKE_EXAMPLE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }

    @GET
    @Path(FIND_LIKE_EXAMPLE_LAZY)
    public Response findLikeExampleLazy(String x) {
        RestAction restAction = () -> {
            List<T> entityList;
            try {
                JSONObject jsonBody = new JSONObject(new JSONTokener(x));
                entityList = getEntityDao().findLikeExample(
                        getJSONEntityWrapper().toEntity(jsonBody.getJSONObject("entity")),
                        jsonBody.getInt("first"),
                        jsonBody.getInt("pageSize"),
                        jsonBody.getString("key"),
                        jsonBody.getBoolean("asc"));
            } catch (JSONException ex) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "Invalid json format in the request body"),
                        getContext(FIND_BY_VALUE));
            }
            return DefaultV1Response.success(arrayFromList(entityList,true),getContext(FIND_LIKE_EXAMPLE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }

    @GET
    @Path(FIND_LIKE_EXAMPLE_COUNT)
    public Response findLikeExampleCount(String x) {
        RestAction restAction = () -> {
            JSONObject jsonBody;
            try {
                jsonBody = new JSONObject(new JSONTokener(x));
            } catch (JSONException ex) {
                return DefaultV1Response.error(DefaultV1Response.errorObject(
                        400,
                        "Invalid json format in the request body"),
                        getContext(FIND_BY_VALUE));
            }
            Long count = getEntityDao().findCountLikeExample(getJSONEntityWrapper().toEntity(jsonBody.getJSONObject("entity")));
            return DefaultV1Response.success(createNullableJSONArray(count),getContext(FIND_LIKE_EXAMPLE));
        };

        return restCall(restAction, FIND_BY_VALUE);
    }

    protected JSONArray arrayFromList(List<T> entityList, boolean hideValues) {
        JSONArray data = new JSONArray();
        for (T entity: entityList) {
            addNullableValue(data, getJSONEntityWrapper().toJSON(entity, hideValues));
        }
        return data;
    }

    public Response restCall(RestAction restAction, String method) {
        try {
            return restAction.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            return DefaultV1Response.error(ex, getContext(method));
        }
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
