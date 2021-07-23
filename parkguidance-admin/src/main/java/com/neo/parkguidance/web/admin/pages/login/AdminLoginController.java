package com.neo.parkguidance.web.admin.pages.login;

import com.neo.parkguidance.web.admin.security.UserBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

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
    UserBean user;

    @Inject
    AdminLoginFacade facade;

    public void login() throws IOException {
        facade.checkCredentials(model, user);
    }

    public AdminLoginModel getModel() {
        return model;
    }
}
