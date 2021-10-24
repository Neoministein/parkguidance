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

public abstract class AbstractBasedAuthentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBasedAuthentication.class);

    @Inject
    SecurityContext securityContext;

    protected AuthenticationStatus login(Credential credential, boolean remember,
            HttpServletRequest request, HttpServletResponse response) {
        return securityContext.authenticate(request, response,
                AuthenticationParameters.withParams().rememberMe(remember).credential(credential));
    }

    public void logout(HttpSession session) {
        LOGGER.info("Login out [{}] user", securityContext.getCallerPrincipal().getName());

        LOGGER.debug("Invalidating session [{}]", session.getId());
        session.invalidate();
    }

    public boolean isLoggedIn() {
        return securityContext.getCallerPrincipal() != null;
    }
}
