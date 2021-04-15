package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.ApiRequest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ApiRequestEntityManager extends AbstractEntityDao<ApiRequest>{

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApiRequestEntityManager() {
        super(ApiRequest.class);
    }
}
