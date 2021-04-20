package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class ParkingGarageEntityManager extends AbstractEntityDao<ParkingGarage> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Inject
    AddressEntityManager addressEntityManager;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void addSubCriteria(Criteria criteria, ParkingGarage object) {
        Criteria subCriteria = criteria.createCriteria(Address.TABLE_NAME, Address.TABLE_NAME);
        Example example = Example.create(object.getAddress()).ignoreCase().enableLike(MatchMode.ANYWHERE);
        subCriteria.add(example);
        addressEntityManager.addSubCriteria(subCriteria, object.getAddress());
    }

    public ParkingGarageEntityManager() {
        super(ParkingGarage.class);
    }

    public ParkingGarage findHighestId() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        AbstractQuery<ParkingGarage> cq1 = cb.createQuery(entityClass);

        Root<ParkingGarage> stud1 = cq1.from(entityClass);

        CriteriaQuery<ParkingGarage> select1 = ((CriteriaQuery<ParkingGarage>) cq1).select(stud1);
        select1.orderBy(cb.desc(stud1.get(ParkingGarage.C_ID)));
        TypedQuery<ParkingGarage> tq1 = getEntityManager().createQuery(select1);
        List<ParkingGarage> list1 = tq1.setMaxResults(1).getResultList();

        if(!list1.isEmpty()) {

            return list1.get(0);
        }
        throw new IllegalArgumentException(getClass().getName() + " no entries found");
    }

    @Override
    public void remove(ParkingGarage entity) {
        super.remove(entity);
        addressEntityManager.remove(entity.getAddress());
    }
}
