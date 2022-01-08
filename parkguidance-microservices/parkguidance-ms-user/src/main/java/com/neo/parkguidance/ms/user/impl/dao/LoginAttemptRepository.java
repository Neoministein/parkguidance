package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.LoginAttempt;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class LoginAttemptRepository extends AbstractEntityDao<LoginAttempt> implements EntityDao<LoginAttempt> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginAttemptRepository() {
        super(LoginAttempt.class);
    }

}
