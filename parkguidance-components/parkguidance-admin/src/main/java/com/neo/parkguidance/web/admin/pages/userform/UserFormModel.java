package com.neo.parkguidance.web.admin.pages.userform;

import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormModel;
import org.omnifaces.cdi.ViewScoped;

import java.util.List;

@ViewScoped
public class UserFormModel extends AbstractFormModel<RegisteredUser> {

    private String newPassword;
    private List<Permission> allPermissions;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<Permission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(List<Permission> allPermissions) {
        this.allPermissions = allPermissions;
    }
}
