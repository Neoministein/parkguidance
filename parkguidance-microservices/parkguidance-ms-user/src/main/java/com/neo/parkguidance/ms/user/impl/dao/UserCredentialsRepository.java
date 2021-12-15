package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.UserCredentials;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class UserCredentialsRepository extends AbstractEntityDao<UserCredentials> implements EntityDao<UserCredentials> {

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
