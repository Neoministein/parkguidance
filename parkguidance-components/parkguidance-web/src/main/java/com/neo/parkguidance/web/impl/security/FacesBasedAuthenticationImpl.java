package com.neo.parkguidance.web.impl.security;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.core.impl.security.AbstractBasedAuthentication;
import com.neo.parkguidance.core.impl.security.token.TokenCredentials;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.web.api.security.FacesBasedAuthentication;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Stateless
public class FacesBasedAuthenticationImpl extends AbstractBasedAuthentication implements FacesBasedAuthentication {

    private static final String COOKIE_USER = "admin-user";
    private static final String COOKIE_PASSWORD = "admin-pass";
    private static final String COOKIE_TIMEOUT = "web.auth.cookie.timeout";

    private static final int DEFAULT_COOKIE_TIMEOUT = 30 * 60; //30 min

    private static final Logger LOGGER = LoggerFactory.getLogger(FacesBasedAuthenticationImpl.class);

    @Inject
    FacesContext facesContext;

    @Inject
    ExternalContext externalContext;

    @Inject
    AdminConfig adminConfig;

    public void attemptCookieBasedLogin() {
        LOGGER.debug("Cookie based login attempted");

        String authToken = Faces.getRequestCookie(AUTH_TOKEN);
        if (!StringUtils.isEmpty(authToken)) {
            login(new TokenCredentials(authToken), false);
            if (!isLoggedIn()) {
                Faces.removeResponseCookie(AUTH_TOKEN,"");
            }
        }
    }

    public void login(String username, String password, boolean remember) {
        login(new UsernamePasswordCredential(username, password), remember);
    }


    protected void login(Credential credential, boolean remember) {
        switch (login(credential, remember,
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse())) {
        case SEND_CONTINUE:
            facesContext.responseComplete();
            break;
        case SEND_FAILURE:
            Messages.addError(null, "Login failed");
            externalContext.getFlash().setKeepMessages(true);
            break;
        case SUCCESS:
            externalContext.getFlash().setKeepMessages(true);
            Utils.addDetailMessage("Logged in successfully");
            redirect(adminConfig.getIndexPage());
            break;
        case NOT_DONE:
            Messages.addError(null, "Login failed");
        }
    }

    public void logout() {
        String loginPage = adminConfig.getLoginPage();
        if (loginPage == null || "".equals(loginPage)) {
            loginPage = "login.xhtml";
        }

        super.logout(Faces.getSession(),
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse());
        redirect(loginPage);
    }

    protected void redirect(String location) {
        if (!StringUtils.isEmpty(location)) {
            if (!location.startsWith("/")) {
                location = "/" + location;
            }
            LOGGER.debug("Redirecting user to {}", location);
            ExternalContext ec = Faces.getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + location);
            } catch (IOException e) {
                LOGGER.warn("Unable to redirect user to {}", location);
            }
        }
    }
}
