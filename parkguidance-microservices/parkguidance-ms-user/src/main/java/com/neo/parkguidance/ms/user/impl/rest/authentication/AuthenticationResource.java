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
                return AuthenticationResponse.error(
                        AuthenticationResponse.CREDENTIALS_INVALID,
                        getContext(HttpMethod.POST, P_CREDENTIALS));
            }

            Response statusResponse = checkUserStatus(registeredUser.getUserStatus(), P_CREDENTIALS);
            if (statusResponse != null) {
                return statusResponse;
            }
            if (!registeredUser.getEmailVerified().booleanValue()) {
                return AuthenticationResponse.partialSuccess(
                        AuthenticationResponse.UNVERIFIED_EMAIL,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                                registeredUser,
                                true,
                                true)),
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
                return AuthenticationResponse.error(0, getContext(HttpMethod.POST, P_TOKEN));
            }
            RegisteredUser registeredUser = userToken.getRegisteredUser();

            Response statusResponse = checkUserStatus(registeredUser.getUserStatus(), P_TOKEN);
            if (statusResponse != null) {
                return statusResponse;
            }

            if (!registeredUser.getEmailVerified().booleanValue()) {
                return AuthenticationResponse.partialSuccess(
                        5,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                                registeredUser,
                                false,
                                true)),
                        getContext(HttpMethod.POST, P_TOKEN)
                );
            }
            if (TokenType.PARTIAL.equals(userToken.getType())) {
                return AuthenticationResponse.partialSuccess(
                        6,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                                registeredUser,
                                false,
                                true)),
                        getContext(HttpMethod.POST, P_TOKEN)
                );
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
                return AuthenticationResponse.error(
                        AuthenticationResponse.ACCOUNT_DEACTIVATED,
                        context);
            case LOCKT_TO_MANY_FAILED_AUTH:
                return AuthenticationResponse.error(
                        AuthenticationResponse.TO_MANY_LOGIN_ATTEMPTS,
                        context);
            case LOCKT_REQUIRE_PASSWORD_RESET:
                return AuthenticationResponse.error(
                        AuthenticationResponse.PASSWORD_REQUIRES_RESET,
                        context);
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
