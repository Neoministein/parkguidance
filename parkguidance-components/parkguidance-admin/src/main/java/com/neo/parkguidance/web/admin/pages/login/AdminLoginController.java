package com.neo.parkguidance.web.admin.pages.login;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the AdminLogin screen
 */
@RequestScoped
@Named(value = AdminLoginController.BEAN_NAME)
public class AdminLoginController {

    public static final String BEAN_NAME = "adminLogin";

    @Inject
    AdminLoginModel model;

    @Inject
    AdminLoginFacade facade;


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

    public AdminLoginModel getModel() {
        return model;
    }
}
