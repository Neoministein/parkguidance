package com.neo.parkguidance.core.impl.security;

import com.neo.parkguidance.core.api.security.ServletBasedAuthentication;
import com.neo.parkguidance.core.impl.security.exception.UnverifiedEmailException;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Stateless
public class ServletBasedAuthentificationImpl extends AbstractBasedAuthentication implements
        ServletBasedAuthentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServletBasedAuthentificationImpl.class);

    @Override
    public void login(Credential credential, String redirectSuccess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            super.login(credential, false, request, response);
        } catch (UnverifiedEmailException ex) {
            LOGGER.info("Email isn't verified");
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Email not is not verified");
            } catch (IOException e) {
                LOGGER.info("Unable to send error message to client");
            }
        }

        if (isLoggedIn()) {
            if (!StringUtils.isEmpty(redirectSuccess)) {
                redirect(redirectSuccess, request, response);
            }
        } else {
            if (!StringUtils.isEmpty(redirectFail)) {
                redirect(redirectFail, request, response);
            }
        }
    }

    protected void redirect(String location, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + location);
        } catch (IOException ex) {
            LOGGER.warn("Unable to redirect user to {}", location);
        }
    }
}
