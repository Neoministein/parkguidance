package com.neo.parkguidance.web.admin.pages.userform;

import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormController;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = UserFormController.BEAN_NAME)
public class UserFormController extends AbstractFormController<RegisteredUser> {

    public static final String BEAN_NAME = "userform";

    @Inject
    UserFormModel model;

    @Inject
    UserFormFacade facade;

    @Override
    public void init() {
        if (!model.isInitialized()) {
            super.init();
            model.setAllPermissions(facade.getAllPermissions());
            model.setNewToken(facade.newUserToken());
            model.setInitialized(true);
        }
    }

    public void createToken() {
        facade.createToken(model.getNewToken(), model.getEntity());
        model.setSelectedToken(model.getNewToken());
        model.setNewToken(facade.newUserToken());
    }

    public void removeToken() {
        facade.removeToken(model.getSelectedToken(), model.getEntity());
    }

    @Override
    protected String getRedirectLocation() {
        return "/admin/user-list";
    }

    @Override
    public UserFormModel getModel() {
        return model;
    }

    @Override
    protected UserFormFacade getFacade() {
        return facade;
    }
}
