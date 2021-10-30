package com.neo.parkguidance.core.impl.auth;

import com.neo.parkguidance.core.api.auth.ServletBasedAuthentication;
import com.neo.parkguidance.core.impl.auth.exception.UnverifiedEmailException;

import javax.ejb.Stateless;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Stateless
public class ServletBasedAuthentificationImpl extends AbstractBasedAuthentication implements
        ServletBasedAuthentication {

    @Override
    public void login(Credential credential, String redirectSuccess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            super.login(credential, false, request, response);
        } catch (UnverifiedEmailException ex) {
            //TODO
        }
        try {
            if (!isLoggedIn()) {
                response.sendRedirect(request.getContextPath() + redirectFail);
            } else {
                response.sendRedirect(request.getContextPath() + redirectSuccess);
            }
        }catch (Exception e) {
            //TODO
        }
    }
}
