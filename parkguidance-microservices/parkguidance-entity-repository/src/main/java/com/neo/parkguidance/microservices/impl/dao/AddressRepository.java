package com.neo.parkguidance.microservices.impl.dao;

import com.neo.parkguidance.microservices.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.api.geomap.GeoCodingService;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.microservices.impl.validation.AddressValidator;
import com.neo.parkguidance.microservices.impl.validation.EntityValidationException;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Priority(100)
@RequestScoped
public class AddressRepository extends AbstractEntityDao<Address> implements EntityDaoAbstraction<Address> {

    @Inject
    AddressValidator addressValidator;

    @Inject
    GeoCodingService geoCodingService;

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AddressRepository() {
        super(Address.class);
    }

    @Override
    public void create(Address address) {
        findCoordinates(address);
        super.create(address);
    }

    @Override
    public void edit(Address address) {
        if (!addressValidator.hasNothingChanged(address)) {
            findCoordinates(address);
        }
        super.edit(address);
    }

    protected void findCoordinates(Address address) {
        try {
            geoCodingService.findCoordinates(address);
        } catch (RuntimeException ex) {
            throw new EntityValidationException(ex);
        }
    }
}
