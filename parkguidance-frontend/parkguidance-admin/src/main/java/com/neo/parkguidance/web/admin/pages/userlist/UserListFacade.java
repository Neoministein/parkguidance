package com.neo.parkguidance.web.admin.pages.userlist;

import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyFacade;
import com.neo.parkguidance.web.impl.table.Filter;

import javax.ejb.Stateless;

@Stateless
public class UserListFacade extends AbstractLazyFacade<RegisteredUser> {
    @Override
    public Filter<RegisteredUser> newFilter() {
        return new Filter<>(new RegisteredUser());
    }
}
