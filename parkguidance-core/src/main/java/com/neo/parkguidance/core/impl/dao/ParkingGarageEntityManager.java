package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ParkingGarageEntityManager extends AbstractEntityFacade<ParkingGarage> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParkingGarageEntityManager() {
        super(ParkingGarage.class);
    }
}
