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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/api/authenticate")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class AuthenticationResource extends AbstractRestEndpoint {

    private static final String CREDENTIALS = "/credentials";
    private static final String TOKEN = "/token";

    @Inject
    JWTGeneratorService jwtGeneratorService;

    @Inject
    CredentialsManagerService credentialsManagerService;

    @POST
    @Path(CREDENTIALS)
    public Response credentials(String x) {
        RestAction action = () -> {
            JSONObject body = new JSONObject(new JSONTokener(x));
            RegisteredCredentials registeredCredentials = new RegisteredCredentials(
                    body.getString("identification"),
                    body.getString("password"),
                    requestDetails.getRemoteAddress(),
                    CREDENTIALS
            );
            RegisteredUser registeredUser = credentialsManagerService.retrieveUser(registeredCredentials);
            if (registeredUser == null) {
                return AuthenticationResponse.error(
                        AuthenticationResponse.CREDENTIALS_INVALID,
                        getContext(HttpMethod.POST, CREDENTIALS));
            }

            Response statusResponse = checkUserStatus(registeredUser.getUserStatus(), CREDENTIALS);
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
                        getContext(HttpMethod.POST, CREDENTIALS));
            }
            return DefaultV1Response.success(
                    getContext(HttpMethod.POST, CREDENTIALS),
                    new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                            registeredUser,
                            true,
                            false)));
        };
        return super.restCall(action, HttpMethod.POST ,CREDENTIALS);
    }

    @POST
    @Path(TOKEN)
    public Response token(String x) {
        RestAction action = () -> {
            JSONObject body = new JSONObject(new JSONTokener(x));
            TokenCredentials registeredCredentials = new TokenCredentials(
                    body.getString("token"),
                    requestDetails.getRemoteAddress(),
                    TOKEN
            );
            UserToken userToken = credentialsManagerService.retrieveUser(registeredCredentials);
            if (userToken == null) {
                return AuthenticationResponse.error(0, getContext(HttpMethod.POST, TOKEN));
            }
            RegisteredUser registeredUser = userToken.getRegisteredUser();

            Response statusResponse = checkUserStatus(registeredUser.getUserStatus(), TOKEN);
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
                        getContext(HttpMethod.POST, TOKEN)
                );
            }
            if (TokenType.PARTIAL.equals(userToken.getType())) {
                return AuthenticationResponse.partialSuccess(
                        6,
                        new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                                registeredUser,
                                false,
                                true)),
                        getContext(HttpMethod.POST, TOKEN)
                );
            }

            return DefaultV1Response.success(
                    getContext(HttpMethod.POST, TOKEN),
                    new JSONArray().put(jwtGeneratorService.generateJWTResponse(
                            registeredUser,
                            false,
                            false)));
        };
        return super.restCall(action, HttpMethod.POST, TOKEN);
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
        return "/api/authenticate";
    }
}
