package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class DataSheetEntityManager extends AbstractEntityDao<DataSheet> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DataSheetEntityManager() {
        super(DataSheet.class);
    }

    public List<DataSheet> findOfHour(int hour, ParkingGarage parkingGarage) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<DataSheet> cq1=cb.createQuery(entityClass);

        Root<DataSheet> stud1=cq1.from(entityClass);

        cq1.where(cb.and(
                cb.equal(stud1.get(DataSheet.C_HALF_HOUR), hour),
                cb.equal(stud1.get(ParkingGarage.TABLE_NAME),parkingGarage)));

        CriteriaQuery<DataSheet> select1 = ((CriteriaQuery<DataSheet>) cq1).select(stud1);
        TypedQuery<DataSheet> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }

}
