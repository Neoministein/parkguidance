package com.neo.parkguidance.core.api.security;

import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This interface defines the capacities of a servlet needs to authenticate
 */
public interface ServletBasedAuthentication {

    /**
     * Tries to authenticate the user based on the credentails
     *
     * @param credential the user credentials
     * @param redirectSuccess the page which a redirection shall occur on success
     * @param redirectFail the page which a redirection shall occur on fail
     * @param request the request of the authentication call
     * @param response the response of the authentication call
     */
    void login(Credential credential, String redirectSuccess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response);

    /**
     * Invalidated the current HTTP session in order to logout the current user
     *
     * @param session the session to validate
     * @param request the request of the authentication call
     * @param response the response of the authentication call
     */
    void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response);
}
