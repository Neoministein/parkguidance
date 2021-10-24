package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.api.auth.ServletBasedAuthentication;
import com.neo.parkguidance.core.impl.auth.credential.OAuthCredential;
import com.neo.parkguidance.core.impl.auth.exception.UnverifiedEmailException;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Stateless
public class ServletBasedAuthentificationImpl extends AbstractBasedAuthentication implements
        ServletBasedAuthentication {

    @Override
    public void login(String oauth2Token, String provider, String redirectSucess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            super.login(new OAuthCredential(oauth2Token, provider), true, request, response);
        } catch (UnverifiedEmailException ex) {
            //TODO
        }
        if (isLoggedIn()) {
            response.sendRedirect(request.getContextPath() + "/index");
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
