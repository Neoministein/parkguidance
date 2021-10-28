package com.neo.parkguidance.core.api.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This interface defines the capacities of a servlet needs to authenticate
 */
public interface ServletBasedAuthentication {

    /**
     * Tries to authenticate the user via a oAuth2 provider
     *
     * @param oAuth2Token the oAuth2Token
     * @param provider the provider of the oAuth2Service
     * @param redirectSuccess the page which a redirection shall occur on success
     * @param redirectFail the page which a redirection shall occur on fail
     * @param request the request of the authentication call
     * @param response the response of the authentication call
     */
    void login(String oAuth2Token, String provider, String redirectSuccess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response);

    /**
     * Invalidated the current HTTP session in order to logout the current user
     *
     * @param session the session to validate
     */
    void logout(HttpSession session);
}
