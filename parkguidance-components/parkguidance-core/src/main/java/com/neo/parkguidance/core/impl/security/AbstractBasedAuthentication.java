package com.neo.parkguidance.core.impl.security;

import com.neo.parkguidance.core.api.security.token.TokenService;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.security.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * This is a abstract implementation for authentication via the {@link SecurityContext}
 */
public abstract class AbstractBasedAuthentication {

    protected static final String AUTH_TOKEN = "token";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBasedAuthentication.class);

    @Inject
    SecurityContext securityContext;

    @Inject
    TokenService tokenService;

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
        AuthenticationStatus authenticationStatus = securityContext.authenticate(request, response,
                AuthenticationParameters.withParams().rememberMe(remember).credential(credential));

        if (AuthenticationStatus.SUCCESS.equals(authenticationStatus) && remember) {
            addTokenToResponse(response);
        }

        return authenticationStatus;
    }

    /**
     * Invalidates the current session
     *
     * @param session the session to invalidate
     */
    public void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Login out [{}] user", securityContext.getCallerPrincipal().getName());

        LOGGER.debug("Invalidating session [{}]", session.getId());
        session.invalidate();

        LOGGER.info("Invalidating token on client side");
        Cookie cookie = new Cookie(AUTH_TOKEN, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        LOGGER.info("Invalidating token on server side");
        for (Cookie clientCookie:  request.getCookies()) {
            if (AUTH_TOKEN.equals(clientCookie.getName())) {
                tokenService.inValidateToken(getRegisteredUserPrincipal().getUserId(), clientCookie.getValue());
                LOGGER.info("Token found and invalidated");
                break;
            }
        }
    }

    /**
     * Checks if the current session is logged in
     *
     * @return true if logged in
     */
    public boolean isLoggedIn() {
        return securityContext.getCallerPrincipal() != null;
    }

    protected RegisteredUserPrincipal getRegisteredUserPrincipal() {
        return (RegisteredUserPrincipal) securityContext.getCallerPrincipal();
    }

    protected void addTokenToResponse(HttpServletResponse httpServletResponse) {
        UserToken generatedToken = tokenService.generateToken(
                getRegisteredUserPrincipal().getUserId(),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60),
                TokenType.UNLIMITED,
                "Cookie auth token");
        Cookie token = new Cookie(AUTH_TOKEN, generatedToken.getKey());
        token.setMaxAge((int) ((generatedToken.getExpirationDate().getTime() - System.currentTimeMillis()) / 1000));
        token.setSecure(true);
        httpServletResponse.addCookie(token);
    }
}
