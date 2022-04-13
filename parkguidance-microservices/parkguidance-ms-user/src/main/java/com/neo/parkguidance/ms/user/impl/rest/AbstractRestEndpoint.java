package com.neo.parkguidance.ms.user.impl.rest;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.ms.user.api.rest.RestAction;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestEndpoint.class);

    protected static final String E_INVALID_JSON = "pgs/json/001";
    protected static final String E_INTERNAL_LOGIC = "pgs/unknown";

    @Inject
    protected RequestDetails requestDetails;

    public Response restCall(RestAction restAction, HttpMethod method, String context) {
        return restCall(restAction, method, context, List.of());
    }

    public Response restCall(RestAction restAction, HttpMethod method, String context, List<String> requiredRoles) {
        MDC.put("traceId", requestDetails.getRequestId());
        LOGGER.debug("{}", getContext(method, context));

        if (!authorized(requiredRoles)) {
            return DefaultV1Response.error(
                    401,
                    getContext(method, context),
                    "pgs/auth/010",
                    "Unauthorized");
        }
        try {
            return restAction.run();
        } catch (JSONException ex) {
            LOGGER.debug("Invalid json format in the request body");
            return DefaultV1Response.error(
                    400,
                    getContext(method, context),
                    E_INVALID_JSON,
                    "Invalid json format in the request body"
            );
        } catch (InternalLogicException ex) {
            LOGGER.error("A exception occurred during a rest call", ex);
            return DefaultV1Response.error(
                    500,
                    getContext(method, context),
                    E_INTERNAL_LOGIC,
                    "Internal server error please try again later"
            );
        } catch (Exception ex) {
            LOGGER.error("A unexpected exception occurred during a rest call", ex);
            return DefaultV1Response.error(
                    500,
                    getContext(method, context),
                    E_INTERNAL_LOGIC,
                    "Internal server error please try again later"
            );
        }
    }

    public boolean authorized(List<String> requiredRoles) {
        for (String role : requiredRoles) {
            if (!requestDetails.isInRole(role)) {
                return false;
            }
        }
        return true;
    }

    protected abstract String getClassURI();

    public String getContext(HttpMethod method, String methodURI) {
        return method + " " + getClassURI() + methodURI;
    }

    //Only for testing purposes
    public void setRequestDetails(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }
}
