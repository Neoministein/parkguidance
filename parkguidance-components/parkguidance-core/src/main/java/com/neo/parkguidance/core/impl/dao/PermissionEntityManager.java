package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.Permission;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PermissionEntityManager extends AbstractEntityDao<Permission> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermissionEntityManager() {
        super(Permission.class);
    }
}
