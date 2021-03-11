package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.entity.ParkingData;
import com.neo.parkguidance.entity.ParkingGarage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

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

    public void create(ParkingGarage parkingGarage, int offset) {
        create(new ParkingData(
                parkingGarage,
                new Date(),
                getCurrentCapacity(parkingGarage) + offset)
        );
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
}
