package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
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
public class ParkingGarageEntityManager extends AbstractEntityDao<ParkingGarage> implements
        EntityDaoAbstraction<ParkingGarage> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Inject EntityDaoAbstraction<Address> abstractAddressDao;

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

    public ParkingGarageEntityManager() {
        super(ParkingGarage.class);
    }

    @Override
    public void remove(ParkingGarage entity) {
        super.remove(entity);
        abstractAddressDao.remove(entity.getAddress());
    }
}
