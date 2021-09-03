package com.neo.parkguidance.web.impl.security;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.web.api.security.UserBasedAuthentication;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserBasedAuthenticationImpl implements UserBasedAuthentication {

    private static final String COOKIE_USER = "admin-user";
    private static final String COOKIE_PASSWORD = "admin-pass";
    private static final String COOKIE_TIMEOUT = "web.cookie.timeout";

    private static final int DEFAULT_COOKIE_TIMEOUT = 1800; //30 min

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBasedAuthenticationImpl.class);

    @Inject
    FacesContext facesContext;

    @Inject
    ExternalContext externalContext;

    @Inject
    AdminConfig adminConfig;

    @Inject
    SecurityContext securityContext;

    @Inject
    StoredValueService storedValueService;


    public String attemptCookieBasedLogin() {
        LOGGER.debug("Cookie based login attempted");
        String emailCookie = Faces.getRequestCookie(COOKIE_USER);
        String passCookie = Faces.getRequestCookie(COOKIE_PASSWORD);
        if (!StringUtils.isEmpty(emailCookie) && !StringUtils.isEmpty(passCookie)) {
            login(emailCookie, passCookie, false);
            return emailCookie;
        }
        return null;
    }

    public void login(String username, String password, boolean remember) {
        switch (continueAuthentication(username, password, remember)) {
        case SEND_CONTINUE:
            facesContext.responseComplete();
            break;
        case SEND_FAILURE:
            Messages.addError(null, "Login failed");
            externalContext.getFlash().setKeepMessages(true);
            break;
        case SUCCESS:
            externalContext.getFlash().setKeepMessages(true);
            Utils.addDetailMessage("Logged in successfully as " + username);
            if (remember) {
                storeCookieCredentials(username, password);
            }
            Faces.redirect(adminConfig.getIndexPage());
            break;
        case NOT_DONE:
            Messages.addError(null, "Login failed");
        }
    }

    public void logout(String user) {
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
        LOGGER.debug("Invalidating session [{}]", Faces.getSession().getId());
        Faces.getSession().invalidate();

        try {
            ExternalContext ec = Faces.getExternalContext();
            ec.redirect(ec.getRequestContextPath() + loginPage);
        } catch (Exception e) {
            LOGGER.warn("Unable to redirect back to login screen");
        }
    }

    public boolean isLoggedIn() {
        return securityContext.getCallerPrincipal() != null;
    }

    private void storeCookieCredentials(final String email, final String password) {
        int cookieTimeOut = storedValueService.getInteger(COOKIE_TIMEOUT, DEFAULT_COOKIE_TIMEOUT);

        Faces.addResponseCookie(COOKIE_USER, email, cookieTimeOut);
        Faces.addResponseCookie(COOKIE_PASSWORD, password, cookieTimeOut);
    }

    private AuthenticationStatus continueAuthentication(String username, String password, boolean remember) {
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams().rememberMe(remember)
                        .credential(new UsernamePasswordCredential(username, password)));
    }
}
