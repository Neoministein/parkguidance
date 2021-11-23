package com.neo.parkguidance.microservices.impl.dao;

import com.neo.parkguidance.microservices.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.entity.Configuration;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Priority(100)
@RequestScoped
public class ConfigurationRepository extends AbstractEntityDao<Configuration> implements
        EntityDaoAbstraction<Configuration> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigurationRepository() {
        super(Configuration.class);
    }
}
