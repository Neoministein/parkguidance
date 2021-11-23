package com.neo.parkguidance.microservices.impl.security;

import com.neo.parkguidance.framework.impl.security.oauth2.OAuthCredential;
import com.neo.parkguidance.framework.impl.security.token.TokenCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
@Secure
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    IdentityStore identityStore;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Credential credential = getCredentialsFromHeader(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));

        CredentialValidationResult result = identityStore.validate(credential);

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            requestContext.setSecurityContext(new CustomSecurityContent(result.getCallerPrincipal(), result.getCallerGroups()));
        } else {
            requestContext.setSecurityContext(new CustomSecurityContent(null, Collections.emptyList()));
        }
    }

    private Credential getCredentialsFromHeader(String header) {
        try {
            JSONObject headerObject = new JSONObject(new JSONTokener(header));

            switch (headerObject.getString("type")) {
            case "JWT":
                return null;
            case "oauth2":
                return new OAuthCredential(headerObject.getString("token"), headerObject.getString("provider"));
            case "credentials":
                return new UsernamePasswordCredential(headerObject.getString("username"),headerObject.getString("password"));
            case "token":
                return new TokenCredentials(headerObject.getString("token"));
            default:
                return null;
            }
        } catch (JSONException ex) {
            return null;
        }
    }
}