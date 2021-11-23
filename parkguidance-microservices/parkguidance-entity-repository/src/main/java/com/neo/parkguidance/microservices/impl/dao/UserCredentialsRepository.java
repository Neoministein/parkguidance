package com.neo.parkguidance.microservices.impl.dao;

import com.neo.parkguidance.microservices.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.entity.UserCredentials;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Priority(100)
@RequestScoped
public class UserCredentialsRepository extends AbstractEntityDao<UserCredentials> implements
        EntityDaoAbstraction<UserCredentials> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserCredentialsRepository() {
        super(UserCredentials.class);
    }
}
