package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.entity.RegisteredUser;
import com.neo.parkguidance.web.infra.entity.AbstractEntityFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserEntityFactory extends AbstractEntityFacade<RegisteredUser> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserEntityFactory() {
        super(RegisteredUser.class);
    }
}
