package com.neo.parkguidance.admin.web.pages.admin.login;

import com.neo.parkguidance.admin.web.security.UserBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@RequestScoped
@Named(value = AdminLoginController.BEAN_NAME)
public class AdminLoginController {

    public static final String BEAN_NAME = "adminLogin";

    @Inject
    private AdminLoginModel model;

    @Inject
    private UserBean user;

    @Inject
    private AdminLoginFacade facade;

    public void login() throws IOException {
        facade.checkCredentials(model, user);
    }

    public AdminLoginModel getModel() {
        return model;
    }
}
