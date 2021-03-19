package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.DataSheet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DataSheetEntityManager extends AbstractEntityFacade<DataSheet> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DataSheetEntityManager() {
        super(DataSheet.class);
    }

}
