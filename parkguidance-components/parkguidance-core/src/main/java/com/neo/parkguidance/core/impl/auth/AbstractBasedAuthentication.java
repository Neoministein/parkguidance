package com.neo.parkguidance.core.impl.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This is a abstract implementation for authentication via the {@link SecurityContext}
 */
public abstract class AbstractBasedAuthentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBasedAuthentication.class);

    @Inject
    SecurityContext securityContext;

    /**
     * Try an authentication attempt via the {@link SecurityContext} with the given credentials
     *
     * @param credential the credentials to authenticate against
     * @param remember if the user shall be renumbered
     * @param request the current request
     * @param response the current response
     *
     * @return status of the authentication result
     */
    protected AuthenticationStatus login(Credential credential, boolean remember,
            HttpServletRequest request, HttpServletResponse response) {
        return securityContext.authenticate(request, response,
                AuthenticationParameters.withParams().rememberMe(remember).credential(credential));
    }

    /**
     * Invalidates the current session
     *
     * @param session the session to invalidate
     */
    public void logout(HttpSession session) {
        LOGGER.info("Login out [{}] user", securityContext.getCallerPrincipal().getName());

        LOGGER.debug("Invalidating session [{}]", session.getId());
        session.invalidate();
    }

    /**
     * Checks if the current session is logged in
     *
     * @return true if logged in
     */
    public boolean isLoggedIn() {
        return securityContext.getCallerPrincipal() != null;
    }
}
