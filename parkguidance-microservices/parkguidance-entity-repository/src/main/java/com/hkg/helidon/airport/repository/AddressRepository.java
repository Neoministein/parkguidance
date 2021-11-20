package com.hkg.helidon.airport.repository;

import com.neo.parkguidance.framework.entity.Address;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class AddressRepository extends AbstractEntityDao<Address> {

    @PersistenceContext()
    private EntityManager em;

    protected AddressRepository() {
        super(Address.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
