package com.neo.parkguidance.parkdata.receiver.impl.security;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * This class checks if the accessKey is valid
 */
@Stateless
public class ParkingGarageAuthentication {

    private static final Logger LOGGER = LogManager.getLogger(ParkingGarageAuthentication.class);

    @Inject
    private AbstractEntityDao<ParkingGarage> parkingGarageManager;

    public ParkingGarage validate(String accessKey) {
        List<ParkingGarage> list = parkingGarageManager.findByColumn(ParkingGarage.C_ACCESS_KEY,accessKey);

        if(!list.isEmpty()) {
            LOGGER.debug("Provided ParkingGarage accessKey is valid");
            return list.get(0);
        } else {
            LOGGER.warn("Provided ParkingGarage accessKey [{}] is invalid", accessKey);
            return null;
        }
    }
}
