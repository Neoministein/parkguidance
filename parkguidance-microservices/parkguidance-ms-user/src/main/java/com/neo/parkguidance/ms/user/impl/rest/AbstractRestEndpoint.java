package com.neo.parkguidance.ms.user.impl.rest;

import com.neo.parkguidance.ms.user.api.RestAction;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public abstract class AbstractRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestEndpoint.class);

    public Response restCall(RestAction restAction, String method) {
        try {
            return restAction.run();
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

    public void test () {
        RestAction restAction = () -> {
            System.out.println();
            throw new RuntimeException();
        };
        restCall(restAction, getContext(""));
    }

    protected abstract String getContext(String method);
}
