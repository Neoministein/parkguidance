package com.neo.parkguidance.web.infra.permission;

import com.neo.parkguidance.entity.Permission;
import com.neo.parkguidance.web.security.UserBean;

import java.util.Set;

public class PermissionFacade {

    private static final String SUPER_USER = "SUPER_USER";

    public boolean has(UserBean loginModel ,String permission) {
        Set<Permission> userPermissions = loginModel.getRegisteredUser().getPermissions();

        for (Permission uPerm: userPermissions) {
            if (uPerm.getName().equals(permission) || uPerm.getName().equals(SUPER_USER)) {
                return true;
            }
        }
        return false;
    }
}
