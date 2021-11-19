package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.ConfigValue;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ConfigValueRepository extends AbstractEntityDao<ConfigValue> implements
        EntityDaoAbstraction<ConfigValue> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigValueRepository() {
        super(ConfigValue.class);
    }
}
