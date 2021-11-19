package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.UserCredentials;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserCredentialsRepository extends AbstractEntityDao<UserCredentials> implements
        EntityDaoAbstraction<UserCredentials> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserCredentialsRepository() {
        super(UserCredentials.class);
    }
}
