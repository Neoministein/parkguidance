package com.neo.parkguidance.framework.impl.dao;

import com.neo.parkguidance.framework.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.entity.Configuration;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ConfigurationRepository extends AbstractEntityDao<Configuration> implements
        EntityDaoAbstraction<Configuration> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigurationRepository() {
        super(Configuration.class);
    }
}
