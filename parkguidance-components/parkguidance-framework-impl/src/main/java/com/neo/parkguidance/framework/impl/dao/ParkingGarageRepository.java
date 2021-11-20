package com.neo.parkguidance.framework.impl.dao;

import com.neo.parkguidance.framework.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.impl.validation.ParkingGarageValidator;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Default
@Stateless
public class ParkingGarageRepository extends AbstractEntityDao<ParkingGarage> implements
        EntityDaoAbstraction<ParkingGarage> {

    @Inject
    ParkingGarageValidator parkingGarageValidator;

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Inject
    EntityDaoAbstraction<Address> abstractAddressDao;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void addSubCriteria(Criteria criteria, ParkingGarage object) {
        Criteria subCriteria = criteria.createCriteria(Address.TABLE_NAME, Address.TABLE_NAME);
        Example example = Example.create(object.getAddress()).ignoreCase().enableLike(MatchMode.ANYWHERE);
        subCriteria.add(example);
        abstractAddressDao.addSubCriteria(subCriteria, object.getAddress());
    }

    public ParkingGarageRepository() {
        super(ParkingGarage.class);
    }

    @Override
    public void create(ParkingGarage parkingGarage) {
        parkingGarageValidator.newUniqueAccessKey(parkingGarage);
        parkingGarageValidator.validatePrimaryKey(parkingGarage.getPrimaryKey());
        if (parkingGarage.getAddress().getId() == null) {
            abstractAddressDao.create(parkingGarage.getAddress());
        }
        super.create(parkingGarage);
    }

    @Override
    public void remove(ParkingGarage entity) {
        super.remove(entity);
        abstractAddressDao.remove(entity.getAddress());
    }
}
