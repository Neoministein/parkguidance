package com.neo.parkguidance.ms.user.impl.rest.authentication;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.ms.user.impl.rest.DefaultV1Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.Response;

public class AuthenticationResponse {

    public static final int INVALID_TOKEN = 0;
    public static final int CREDENTIALS_INVALID = 1;
    public static final int ACCOUNT_DEACTIVATED = 2;
    public static final int TO_MANY_LOGIN_ATTEMPTS = 3;
    public static final int PASSWORD_REQUIRES_RESET = 4;
    public static final int UNVERIFIED_EMAIL = 5;
    public static final int PARTIAL_LOGIN_TOKEN = 6;

    private AuthenticationResponse() {}

    public static Response error(int errorCode, String context) {
        switch (errorCode) {
            case INVALID_TOKEN:
                return DefaultV1Response.error(
                        401,
                        context,
                        "pgs/auth/000",
                        "Provided token invalid");
            case CREDENTIALS_INVALID:
                return DefaultV1Response.error(
                    401,
                    context,
                    "pgs/auth/001",
                    "Credentials authentication failed");
            case ACCOUNT_DEACTIVATED:
                return DefaultV1Response.error(
                        401,
                        context,
                        "pgs/auth/002",
                        "This account is deactivated");
            case TO_MANY_LOGIN_ATTEMPTS:
                return DefaultV1Response.error(
                    401,
                    context,
                    "pgs/auth/003",
                    "To many failed authentication attempts");
            case PASSWORD_REQUIRES_RESET:
                return DefaultV1Response.error(
                        401,
                        context,
                        "pgs/auth/004",
                        "This account requires a password reset");
            default:
                throw new InternalLogicException("Unknown authentication response");
        }
    }

    public static Response partialSuccess(int errorCode, JSONArray data, String context) {
        JSONObject response = DefaultV1Response.defaultResponse(200, context);
        response.put("data", data);

        switch (errorCode) {
            case UNVERIFIED_EMAIL:
                response.put("error", DefaultV1Response.errorArray(
                    "pgs/auth/005",
                    "Unverified email"));
                break;
            case PARTIAL_LOGIN_TOKEN:
                response.put("error", DefaultV1Response.errorObject(
                    "pgs/auth/006",
                    "Provided partial login token"));
                break;
            default:
                throw new InternalLogicException("Unknown authentication response");
        }

        return Response.ok().entity(response).build();
    }
}
