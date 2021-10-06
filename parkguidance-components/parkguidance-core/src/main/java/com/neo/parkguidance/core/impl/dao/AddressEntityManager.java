package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.Address;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//TODO Implement ADD GEOLOCATION TO AN INTERFACE IN CORE IN ORDER TO GET GEOLOCATION IN THIS CLASS
@Stateless
public class AddressEntityManager extends AbstractEntityDao<Address> implements EntityDaoAbstraction<Address> {

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
