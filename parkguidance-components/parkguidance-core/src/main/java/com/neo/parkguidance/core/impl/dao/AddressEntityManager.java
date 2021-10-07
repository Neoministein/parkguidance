package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.api.geomap.GeoCodingService;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.validation.AddressValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AddressEntityManager extends AbstractEntityDao<Address> implements EntityDaoAbstraction<Address> {

    @Inject
    AddressValidator addressValidator;

    @Inject
    GeoCodingService geoCodingService;

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AddressEntityManager() {
        super(Address.class);
    }

    @Override
    public void create(Address address) {
        geoCodingService.findCoordinates(address);
        super.create(address);
    }

    @Override
    public void edit(Address address) {
        if (!addressValidator.hasNothingChanged(address)) {
            geoCodingService.findCoordinates(address);
        }
        super.edit(address);
    }
}
