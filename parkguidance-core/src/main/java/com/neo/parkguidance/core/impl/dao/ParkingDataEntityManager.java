package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.ejb.Stateless;
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
public class ParkingDataEntityManager extends AbstractEntityFacade<ParkingData> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParkingDataEntityManager() {
        super(ParkingData.class);
    }

    public int getCurrentCapacity(ParkingGarage parkingGarage) {
        ParkingData newestData = new ParkingData();
        newestData.setDate(new Date(0));
        newestData.setOccupied(0);
        for(ParkingData parkingData: findByColumn(ParkingGarage.TABLE_NAME, parkingGarage)) {
            if (parkingData.getDate().getTime() > newestData.getDate().getTime()) {
                newestData = parkingData;
            }
        }
        return newestData.getOccupied();
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
        List<ParkingData> list1 = tq1.getResultList();

        return list1;
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
        List<ParkingData> list1 = tq1.getResultList();

        return list1;
    }
}
