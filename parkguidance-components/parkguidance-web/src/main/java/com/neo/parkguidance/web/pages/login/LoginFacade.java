package com.neo.parkguidance.web.pages.login;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.web.api.security.UserBasedAuthentication;
import org.omnifaces.util.Faces;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * The screen facade for the AdminLogin screen
 */
@Stateless
public class LoginFacade {

    @Inject
    UserBasedAuthentication userAuthentication;

    @Inject
    AdminConfig adminConfig;

    public void autoLogin(LoginModel model) {
        if (isLoggedIn()) {
            Faces.redirect(adminConfig.getIndexPage());
            return;
        }

        model.setUsername(userAuthentication.attemptCookieBasedLogin());
    }

    public void login(String username, String password, boolean remember) {
        userAuthentication.login(username, password, remember);
    }

    public boolean isLoggedIn() {
        return userAuthentication.isLoggedIn();
    }

    public void logout(String user) {
        userAuthentication.logout(user);
    }
}
