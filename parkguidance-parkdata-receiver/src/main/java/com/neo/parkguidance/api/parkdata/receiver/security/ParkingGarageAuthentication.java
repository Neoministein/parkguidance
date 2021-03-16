package com.neo.parkguidance.api.parkdata.receiver.security;

import com.neo.parkguidance.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityManager;

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
