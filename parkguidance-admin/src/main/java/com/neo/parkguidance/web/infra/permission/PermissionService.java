package com.neo.parkguidance.web.infra.permission;

import com.neo.parkguidance.web.security.UserBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = PermissionService.BEAN_NAME)
public class PermissionService {

    public static final String BEAN_NAME = "permService";

    @Inject
    private UserBean model;

    @Inject
    private PermissionFacade facade;

    public boolean has(String permission) {
        return facade.has(model, permission);
    }
}
