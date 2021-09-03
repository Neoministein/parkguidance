package com.neo.parkguidance.web.pages.login;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the AdminLogin screen
 */
@RequestScoped
@Named(value = LoginController.BEAN_NAME)
public class LoginController {

    public static final String BEAN_NAME = "adminLogin";

    @Inject LoginModel model;

    @Inject LoginFacade facade;


    public void autoLogin() {
        facade.autoLogin(model);
    }

    public void login() {
        facade.login(model.getUsername(), model.getPassword(), model.isRemember());
    }

    public boolean isLoggedIn() {
        return facade.isLoggedIn();
    }

    public void logout() {
        facade.logout(model.getUsername());
    }

    public LoginModel getModel() {
        return model;
    }
}
