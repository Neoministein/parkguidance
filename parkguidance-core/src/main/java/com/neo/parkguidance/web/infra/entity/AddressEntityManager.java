package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.entity.Address;

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
