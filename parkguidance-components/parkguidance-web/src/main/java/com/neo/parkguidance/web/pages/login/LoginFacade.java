package com.neo.parkguidance.web.pages.login;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.core.api.security.oauth2.OAuth2Client;
import com.neo.parkguidance.web.api.security.FacesBasedAuthentication;
import org.omnifaces.util.Faces;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * The screen facade for the AdminLogin screen
 */
@Stateless
public class LoginFacade {

    @Inject
    Instance<OAuth2Client> oAuth2Clients;

    @Inject
    FacesBasedAuthentication userAuthentication;

    @Inject
    AdminConfig adminConfig;

    public void autoLogin() {
        if (isLoggedIn()) {
            Faces.redirect(adminConfig.getIndexPage());
            return;
        }
    }

    public void login(String username, String password, boolean remember) {
        userAuthentication.login(username, password, remember);
    }

    public boolean isLoggedIn() {
        return userAuthentication.isLoggedIn();
    }

    public void logout() {
        userAuthentication.logout();
    }

    public Instance<OAuth2Client> getoAuth2Clients() {
        return oAuth2Clients;
    }
}
