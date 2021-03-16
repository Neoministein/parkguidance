package com.neo.parkguidance.parkdata.receiver.impl.security;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ParkingGarageAuthentication {

    @Inject
    private ParkingGarageEntityManager parkingGarageManager;

    public ParkingGarage validate(String accessKey) {
        List<ParkingGarage> list = parkingGarageManager.findByColumn(ParkingGarage.C_ACCESS_KEY,accessKey);

        if(!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
