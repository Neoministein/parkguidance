package com.neo.parkguidance.web.admin.pages.userform;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.validation.UserTokenValidator;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormFacade;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.primefaces.PrimeFaces;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Stateless
public class UserFormFacade extends AbstractFormFacade<RegisteredUser> {

    @Inject
    UserTokenValidator userTokenValidator;

    @Inject
    EntityDao<Permission> permissionDao;

    @Inject
    EntityDao<UserToken> userTokenDao;

    public void removeToken(UserToken userToken, RegisteredUser registeredUser) {
        registeredUser.getTokens().remove(userToken);
        userTokenDao.remove(userToken);
        Utils.addDetailMessage("Removed UserToken: " + userToken.getName());
        PrimeFaces.current().ajax().update(":tokenForm:userTokenTable");
    }

    public UserToken createToken(UserToken userToken, RegisteredUser registeredUser) {
        userTokenDao.create(userToken);
        //TODO FIND OUT WHY IT NEED TIME OR OTHERWISE DOESN'T FIND IT
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        registeredUser.getTokens().add(userToken);
        super.edit(registeredUser);
        return userToken;
    }

    @Override
    public RegisteredUser findEntityById(Object key) {
        return lazyLoadData(dao.find(key));
    }

    private RegisteredUser lazyLoadData(RegisteredUser user) {
        user.getTokens().size();
        return user;
    }

    @Override
    public void edit(RegisteredUser registeredUser) {
        if (Boolean.TRUE.equals(registeredUser.getDeactivated()) && registeredUser.getDeactivatedOn() == null) {
            registeredUser.setDeactivatedOn(new Date());
        }
        super.edit(registeredUser);
    }

    public List<Permission> getAllPermissions() {
        return permissionDao.findAll();
    }

    @Override
    public RegisteredUser newEntity() {
        return new RegisteredUser();
    }

    public UserToken newUserToken() {
        return new UserToken();
    }
}
