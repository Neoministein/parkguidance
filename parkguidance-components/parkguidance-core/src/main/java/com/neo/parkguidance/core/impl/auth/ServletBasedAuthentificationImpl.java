package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.api.auth.ServletBasedAuthentication;
import com.neo.parkguidance.core.impl.auth.credential.OAuthCredential;
import com.neo.parkguidance.core.impl.auth.exception.UnverifiedEmailException;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Stateless
public class ServletBasedAuthentificationImpl extends AbstractBasedAuthentication implements
        ServletBasedAuthentication {

    @Override
    public void login(String oAuth2Token, String provider, String redirectSuccess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response)  {
        try {
            super.login(new OAuthCredential(oAuth2Token, provider), true, request, response);
        } catch (UnverifiedEmailException ex) {
            //TODO
        }
        try {
            if (isLoggedIn()) {
                response.sendRedirect(request.getContextPath() + "/index");
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        }catch (Exception e) {

        }

    }
}
