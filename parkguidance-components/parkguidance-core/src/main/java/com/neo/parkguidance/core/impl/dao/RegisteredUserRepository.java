package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.impl.validation.RegisteredUserValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless
public class RegisteredUserRepository extends AbstractEntityDao<RegisteredUser> implements
        EntityDaoAbstraction<RegisteredUser> {

    @Inject
    RegisteredUserValidator registeredUserValidator;

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredUserRepository() {
        super(RegisteredUser.class);
    }

    @Override
    public void create(RegisteredUser registeredUser) {
        registeredUserValidator.validatePassword(registeredUser.getPassword());
        registeredUser.setPassword(registeredUserValidator.hashPassword(registeredUser.getPassword()));
        registeredUser.setCreatedOn(new Date());
        super.create(registeredUser);
    }

    @Override
    public void edit(RegisteredUser registeredUser) {
        registeredUserValidator.validatePassword(registeredUser.getPassword());
        registeredUser.setPassword(registeredUserValidator.hashPassword(registeredUser.getPassword()));
        if (Boolean.TRUE.equals(registeredUser.getDeactivated()) && registeredUser.getDeactivatedOn() == null) {
            registeredUser.setDeactivatedOn(new Date());
        }
        if (Boolean.FALSE.equals(registeredUser.getDeactivated() && registeredUser.getDeactivatedOn() != null)) {
            registeredUser.setDeactivatedOn(null);
        }
        super.edit(registeredUser);
    }
}
