package com.neo.parkguidance.microservices.impl.dao;

import com.neo.parkguidance.framework.entity.GarageInfo;
import com.neo.parkguidance.microservices.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.microservices.impl.validation.ParkingGarageValidator;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class ParkingGarageRepository extends AbstractEntityDao<ParkingGarage> implements
        EntityDaoAbstraction<ParkingGarage> {

    @Inject
    ParkingGarageValidator parkingGarageValidator;

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Inject
    EntityDaoAbstraction<Address> abstractAddressDao;

    @Inject
    EntityDaoAbstraction<GarageInfo> abstractGarageInfoDao;

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
        parkingGarage.setOccupied(-1);
        parkingGarageValidator.newUniqueAccessKey(parkingGarage);
        parkingGarageValidator.validatePrimaryKey(parkingGarage.getPrimaryKey());
        if (parkingGarage.getAddress().getId() == null) {
            abstractAddressDao.create(parkingGarage.getAddress());
        }
        super.create(parkingGarage);

        for (GarageInfo garageInfo: parkingGarage.getGarageInfo()) {
            garageInfo.setParkingGarage(parkingGarage);
            abstractGarageInfoDao.create(garageInfo);
        }
    }

    @Override
    public void remove(ParkingGarage entity) {
        super.remove(entity);
        abstractAddressDao.remove(entity.getAddress());
    }
}
