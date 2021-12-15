package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class RegisteredUserRepository extends AbstractEntityDao<RegisteredUser> implements EntityDao<RegisteredUser> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredUserRepository() {
        super(RegisteredUser.class);
    }

}
