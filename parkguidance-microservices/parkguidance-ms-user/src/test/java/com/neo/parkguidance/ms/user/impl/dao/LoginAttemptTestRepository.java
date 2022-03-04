package com.neo.parkguidance.ms.user.impl.dao;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Specializes
@RequestScoped
public class LoginAttemptTestRepository extends LoginAttemptRepository {

    @PersistenceContext(unitName = "parkguidanceTestPersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
