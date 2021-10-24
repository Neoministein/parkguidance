package com.neo.parkguidance.web.impl.security;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.impl.auth.AbstractBasedAuthentication;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.web.api.security.FacesBasedAuthentication;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Inject
    ConfigService configService;


    public String attemptCookieBasedLogin() {
        LOGGER.debug("Cookie based login attempted");
        String emailCookie = Faces.getRequestCookie(COOKIE_USER);
        String passCookie = Faces.getRequestCookie(COOKIE_PASSWORD);
        if (!StringUtils.isEmpty(emailCookie) && !StringUtils.isEmpty(passCookie)) {
            login(emailCookie, passCookie, false);
            if (!isLoggedIn()) {
                return emailCookie;
            }
            Faces.removeResponseCookie(COOKIE_USER,"");
            Faces.removeResponseCookie(COOKIE_PASSWORD,"");
        }
        return null;
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
            if (remember) {
                //TODO
                //storeCookieCredentials(username, password);
            }
            Faces.redirect(adminConfig.getIndexPage());
            break;
        case NOT_DONE:
            Messages.addError(null, "Login failed");
        }
    }

    public void logout(String user) {
        super.logout(Faces.getSession());
        LOGGER.info("Login out [{}] user", user);

        LOGGER.debug("Invalidating authentication cookies");
        if(!StringUtils.isEmpty(Faces.getRequestCookie(COOKIE_USER))) {
            Faces.removeResponseCookie(COOKIE_USER,null);
            Faces.removeResponseCookie(COOKIE_PASSWORD,null);
        }

        String loginPage = adminConfig.getLoginPage();
        if (loginPage == null || "".equals(loginPage)) {
            loginPage = "login.xhtml";
        }

        if (!loginPage.startsWith("/")) {
            loginPage = "/" + loginPage;
        }

        try {
            ExternalContext ec = Faces.getExternalContext();
            ec.redirect(ec.getRequestContextPath() + loginPage);
        } catch (Exception e) {
            LOGGER.warn("Unable to redirect back to login screen");
        }
    }

    private void storeCookieCredentials(final String email, final String password) {
        int cookieTimeOut = configService.getInteger(COOKIE_TIMEOUT, DEFAULT_COOKIE_TIMEOUT);

        Faces.addResponseCookie(COOKIE_USER, email, cookieTimeOut);
        Faces.addResponseCookie(COOKIE_PASSWORD, password, cookieTimeOut);
    }

}
