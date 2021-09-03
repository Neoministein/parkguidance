package com.neo.parkguidance.web.impl.security;

import com.neo.parkguidance.web.api.security.UserBasedAuthentication;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(AutoLogin.BEAN_NAME)
public class AutoLogin {

    public static final String BEAN_NAME = "autologin";

    @Inject
    UserBasedAuthentication userAuthentication;

    public void login() {
        if (!userAuthentication.isLoggedIn()) {
            userAuthentication.attemptCookieBasedLogin();
        }
    }

}
