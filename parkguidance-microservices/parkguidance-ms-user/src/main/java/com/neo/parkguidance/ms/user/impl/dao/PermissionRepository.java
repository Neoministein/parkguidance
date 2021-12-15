package com.neo.parkguidance.ms.user.impl.dao;


import com.neo.parkguidance.ms.user.impl.entity.Permission;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class PermissionRepository extends AbstractEntityDao<Permission> implements EntityDao<Permission> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermissionRepository() {
        super(Permission.class);
    }
}
