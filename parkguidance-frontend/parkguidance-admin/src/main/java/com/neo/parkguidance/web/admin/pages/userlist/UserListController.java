package com.neo.parkguidance.web.admin.pages.userlist;

import com.neo.parkguidance.framework.entity.RegisteredUser;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyController;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyFacade;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value = UserListController.BEAN_NAME)
public class UserListController extends AbstractLazyController<RegisteredUser> {

    public static final String BEAN_NAME = "userlist";

    @Inject
    UserListModel model;

    @Inject
    UserListFacade facade;

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    public AbstractLazyModel<RegisteredUser> getModel() {
        return model;
    }

    @Override
    protected AbstractLazyFacade<RegisteredUser> getFacade() {
        return facade;
    }
}
