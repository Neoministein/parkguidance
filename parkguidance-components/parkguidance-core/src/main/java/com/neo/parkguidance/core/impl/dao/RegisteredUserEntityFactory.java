package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.RegisteredUser;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless
public class RegisteredUserEntityFactory extends AbstractEntityDao<RegisteredUser> implements
        EntityDaoAbstraction<RegisteredUser> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredUserEntityFactory() {
        super(RegisteredUser.class);
    }

    @Override
    public void create(RegisteredUser entity) {
        entity.setCreatedOn(new Date());
        super.create(entity);
    }

    @Override
    public void edit(RegisteredUser registeredUser) {
        if (Boolean.TRUE.equals(registeredUser.getDeactivated()) && registeredUser.getDeactivatedOn() == null) {
            registeredUser.setDeactivatedOn(new Date());
        }
        if (Boolean.FALSE.equals(registeredUser.getDeactivated() && registeredUser.getDeactivatedOn() != null)) {
            registeredUser.setDeactivatedOn(null);
        }
    }
}
