package com.neo.parkguidance.admin.web.security;

import com.github.adminfaces.template.session.AdminSession;
import com.neo.parkguidance.core.entity.RegisteredUser;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Named;

@SessionScoped
@Specializes
@Named(value = UserBean.BEAN_NAME)
public class UserBean extends AdminSession {
    public static final String BEAN_NAME = "userBean";

    private RegisteredUser registeredUser;

    @PostConstruct
    public void init() {
        setIsLoggedIn(false);
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }
}
