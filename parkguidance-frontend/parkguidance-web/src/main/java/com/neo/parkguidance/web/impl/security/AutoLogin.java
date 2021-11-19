package com.neo.parkguidance.web.impl.security;

import com.neo.parkguidance.web.api.security.FacesBasedAuthentication;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Stateless
@Named(AutoLogin.BEAN_NAME)
public class AutoLogin {

    public static final String BEAN_NAME = "autologin";

    @Inject
    FacesBasedAuthentication userAuthentication;

    public void login() {
        if (!userAuthentication.isLoggedIn()) {
            userAuthentication.attemptCookieBasedLogin();
        }
    }
}
