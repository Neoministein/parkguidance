package com.neo.parkguidance.ms.user.impl.rest.authentication;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.ms.user.api.rest.RestAction;
import com.neo.parkguidance.ms.user.api.security.CredentialsManagerService;
import com.neo.parkguidance.ms.user.api.security.jwt.JWTGeneratorService;
import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.impl.rest.AbstractRestEndpoint;
import com.neo.parkguidance.ms.user.impl.rest.DefaultV1Response;
import com.neo.parkguidance.ms.user.impl.rest.HttpMethod;
import com.neo.parkguidance.ms.user.impl.security.TokenType;
import com.neo.parkguidance.ms.user.impl.security.UserStatus;
import com.neo.parkguidance.ms.user.impl.security.credentail.RegisteredCredentials;
import com.neo.parkguidance.ms.user.impl.security.credentail.TokenCredentials;
import io.helidon.security.SecurityContext;
import io.helidon.security.annotations.Authenticated;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path(AuthenticationResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class AuthenticationResource extends AbstractRestEndpoint {

    public static final String RESOURCE_LOCATION = "/api/authenticate";

    public static final String P_CREDENTIALS = "/credentials";
    public static final String P_TOKEN = "/token";

    protected static final JSONObject E_TOKEN_INVALID =         DefaultV1Response.errorObject("pgs/auth/000", "Provided token invalid");
    protected static final JSONObject E_AUTH_FAILED =           DefaultV1Response.errorObject("pgs/auth/001", "Credentials authentication failed");
    protected static final JSONObject E_ACC_DEACTIVATED =       DefaultV1Response.errorObject("pgs/auth/002", "This account is deactivated");
    protected static final JSONObject E_TOO_MANY_AUTH =         DefaultV1Response.errorObject("pgs/auth/003", "To many failed authentication attempts");
    protected static final JSONObject E_PASS_RESET_REQUIRED =   DefaultV1Response.errorObject("pgs/auth/004", "This account requires a password reset");

    protected static final JSONObject E_UNVERIFIED_MAIL =       DefaultV1Response.errorObject("pgs/auth/005", "Unverified email");
    protected static final JSONObject E_PARTIAL_LOGIN =         DefaultV1Response.errorObject("pgs/auth/006", "Provided partial login token");


    @Inject
    JWTGeneratorService jwtGeneratorService;

    @Inject
    CredentialsManagerService credentialsManagerService;

    @GET
    @Authenticated(optional = true)
    public Response get(@Context SecurityContext securityContext) {
        if (securityContext.isAuthenticated()) {
            return Response.ok().entity("{ \"status\": 200}").build();
        }

        return Response.ok().entity("{ \"status\": 401}").build();
    }


    @POST
    @Path(P_CREDENTIALS)
    @Transactional
    public Response credentials(String x) {
        RestAction action = () -> {
            JSONObject body = new JSONObject(new JSONTokener(x));
            RegisteredCredentials registeredCredentials = new RegisteredCredentials(
                    body.getString("identification"),
                    body.getString("password"),
                    requestDetails.getRemoteAddress(), P_CREDENTIALS
            );
            RegisteredUser registeredUser = credentialsManagerService.retrieveUser(registeredCredentials);
            if (registeredUser == null) {
                return DefaultV1Response.error(401, E_AUTH_FAILED, getContext(HttpMethod.POST, P_CREDENTIALS));
            }

            Response statusResponse = checkUserStatus(registeredUser.getUserStatus(), P_CREDENTIALS);
            if (statusResponse != null) {
                return statusResponse;
            }
            if (!registeredUser.getEmailVerified().booleanValue()) {
                return DefaultV1Response.partialSuccess(
                        202, E_UNVERIFIED_MAIL,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(registeredUser,true,true)),
                        getContext(HttpMethod.POST, P_CREDENTIALS));
            }
            return DefaultV1Response.success(
                    getContext(HttpMethod.POST, P_CREDENTIALS),
                    new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                            registeredUser,
                            true,
                            false)));
        };
        return super.restCall(action, HttpMethod.POST , P_CREDENTIALS);
    }

    @POST
    @Path(P_TOKEN)
    @Transactional
    public Response token(String x) {
        RestAction action = () -> {
            JSONObject body = new JSONObject(new JSONTokener(x));
            TokenCredentials registeredCredentials = new TokenCredentials(
                    body.getString("token"),
                    requestDetails.getRemoteAddress(), P_TOKEN
            );
            UserToken userToken = credentialsManagerService.retrieveUser(registeredCredentials);
            if (userToken == null) {
                return DefaultV1Response.error(401, E_TOKEN_INVALID, getContext(HttpMethod.POST, P_TOKEN));
            }
            RegisteredUser registeredUser = userToken.getRegisteredUser();

            Response statusResponse = checkUserStatus(registeredUser.getUserStatus(), P_TOKEN);
            if (statusResponse != null) {
                return statusResponse;
            }

            if (!registeredUser.getEmailVerified().booleanValue()) {
                return DefaultV1Response.partialSuccess(
                        202, E_UNVERIFIED_MAIL,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(registeredUser,false,true)),
                        getContext(HttpMethod.POST, P_TOKEN));
            }
            if (TokenType.PARTIAL.equals(userToken.getType())) {
                return DefaultV1Response.partialSuccess(
                        202,
                        E_PARTIAL_LOGIN,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(registeredUser,false,true)),
                        getContext(HttpMethod.POST, P_TOKEN));
            }

            return DefaultV1Response.success(
                    getContext(HttpMethod.POST, P_TOKEN),
                    new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                            registeredUser,
                            false,
                            false)));
        };
        return super.restCall(action, HttpMethod.POST, P_TOKEN);
    }

    protected Response checkUserStatus(UserStatus userStatus, String context) {
        switch (userStatus) {
            case DEACTIVATED:
                return DefaultV1Response.error(401, E_ACC_DEACTIVATED, context);
            case LOCKET_TO_MANY_FAILED_AUTH:
                return DefaultV1Response.error(401, E_TOO_MANY_AUTH, context);
            case LOCKET_REQUIRE_PASSWORD_RESET:
                return DefaultV1Response.error(401, E_PASS_RESET_REQUIRED, context);
            case OK:
                return null;
            default:
                throw new InternalLogicException("Unknown userStatus");
        }
    }

    @Override
    protected String getClassURI() {
        return RESOURCE_LOCATION;
    }
}
