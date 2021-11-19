package com.hkg.helidon.airport.repository;

import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class ParkingGarageRepository extends AbstractEntityDao<ParkingGarage> {

    @PersistenceContext()
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParkingGarageRepository() {
        super(ParkingGarage.class);
    }
}
