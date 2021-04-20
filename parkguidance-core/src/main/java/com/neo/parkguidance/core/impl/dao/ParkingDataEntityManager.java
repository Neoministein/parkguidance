package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.ParkingData;
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
import java.util.Date;
import java.util.List;

@Stateless
public class ParkingDataEntityManager extends AbstractEntityDao<ParkingData> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Inject
    ParkingGarageEntityManager parkingGarageEntityManager;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void addSubCriteria(Criteria criteria, ParkingData object) {
        Criteria subCriteria = criteria.createCriteria(ParkingGarage.TABLE_NAME);
        Example example = Example.create(object.getParkingGarage()).ignoreCase().enableLike(MatchMode.ANYWHERE);
        subCriteria.add(example);
        parkingGarageEntityManager.addSubCriteria(subCriteria, object.getParkingGarage());
    }

    public ParkingDataEntityManager() {
        super(ParkingData.class);
    }

    public List<ParkingData> getBetweenDate(Date first, Date second, ParkingGarage parkingGarage) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<ParkingData> cq1=cb.createQuery(entityClass);

        Root<ParkingData> stud1=cq1.from(entityClass);

        cq1.where(cb.and(
                cb.between(stud1.get(ParkingData.C_DATE), first, second),
                cb.equal(stud1.get(ParkingGarage.TABLE_NAME),parkingGarage)));

        CriteriaQuery<ParkingData> select1 = ((CriteriaQuery<ParkingData>) cq1).select(stud1);
        TypedQuery<ParkingData> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }

    public List<ParkingData> getBetweenDate(Date first, Date second) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<ParkingData> cq1=cb.createQuery(entityClass);

        Root<ParkingData> stud1=cq1.from(entityClass);

        cq1.where(cb.between(stud1.get(ParkingData.C_DATE), first, second));

        CriteriaQuery<ParkingData> select1 = ((CriteriaQuery<ParkingData>) cq1).select(stud1);
        TypedQuery<ParkingData> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }

    public List<ParkingData> findUnsorted() {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<ParkingData> cq1= cb.createQuery(entityClass);

        Root<ParkingData> stud1=cq1.from(entityClass);

        cq1.where(cb.equal(stud1.get(ParkingData.C_SORTED), false));


        CriteriaQuery<ParkingData> select1 = ((CriteriaQuery<ParkingData>) cq1).select(stud1);
        select1.orderBy(cb.asc(stud1.get(ParkingData.C_DATE)));
        TypedQuery<ParkingData> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }

    public List<ParkingData> getBeforeDate(Date date, ParkingGarage parkingGarage) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<ParkingData> cq1=cb.createQuery(entityClass);

        Root<ParkingData> stud1=cq1.from(entityClass);

        cq1.where(cb.and(
                cb.lessThan(stud1.get(ParkingData.C_DATE), date),
                cb.equal(stud1.get(ParkingGarage.TABLE_NAME),parkingGarage)));

        CriteriaQuery<ParkingData> select1 = ((CriteriaQuery<ParkingData>) cq1).select(stud1);
        TypedQuery<ParkingData> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }

    public List<ParkingData> getBeforeDate(Date date) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<ParkingData> cq1=cb.createQuery(entityClass);

        Root<ParkingData> stud1=cq1.from(entityClass);

        cq1.where(cb.lessThan(stud1.get(ParkingData.C_DATE), date));

        CriteriaQuery<ParkingData> select1 = ((CriteriaQuery<ParkingData>) cq1).select(stud1);
        TypedQuery<ParkingData> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }
}
