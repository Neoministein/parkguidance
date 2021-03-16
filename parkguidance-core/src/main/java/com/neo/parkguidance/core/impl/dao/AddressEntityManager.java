package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.Address;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AddressEntityManager extends AbstractEntityFacade<Address> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AddressEntityManager() {
        super(Address.class);
    }

}
