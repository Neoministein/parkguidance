package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.Role;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class RoleRepository extends AbstractEntityDao<Role> implements EntityDao<Role> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleRepository() {
        super(Role.class);
    }
}
