package com.neo.parkguidance.web.admin.pages.userform;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormFacade;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UserFormFacade extends AbstractFormFacade<RegisteredUser> {

    @Inject
    EntityDao<Permission> permissionDao;

    public List<Permission> getAllPermissions() {
        return permissionDao.findAll();
    }

    @Override
    public RegisteredUser newEntity() {
        return new RegisteredUser();
    }
}
