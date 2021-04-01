package com.neo.parkguidance.parkdata.receiver.impl;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
import com.neo.parkguidance.parkdata.receiver.impl.security.ParkingGarageAuthentication;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Stateless
public class ParkingServiceFacade {

    private static final Object lock = new Object();

    private static final int INCREASE = 1;
    private static final int DECREASE = -1;

    @Inject
    ParkingGarageAuthentication authentication;

    @Inject
    ParkingDataEntityManager parkingDataManager;

    @Inject
    ParkingGarageEntityManager parkingGarageManager;

    public int updateParkingData(String requestString) {
        try {
            synchronized (lock) {
                JSONObject requestData = new JSONObject(requestString);
                ParkingGarage parkingGarage = authentication.validate(requestData.getString(ParkingGarage.C_ACCESS_KEY));

                if(parkingGarage == null) {
                    return HttpServletResponse.SC_FORBIDDEN;
                }

                switch (requestData.getString("type")) {
                case "incr":
                    updateOccupied(parkingGarage, INCREASE);
                    break;
                case "decr":
                    updateOccupied(parkingGarage, DECREASE);
                    break;
                default:
                    return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
                }
                return HttpServletResponse.SC_ACCEPTED;
            }
        }catch (JSONException ex) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
    }

    public void updateOccupied(ParkingGarage garage , int offset) {
        garage.setOccupied(garage.getOccupied() + offset);

        parkingGarageManager.edit(garage);
        parkingDataManager.create(new ParkingData(
                garage,
                new Date(),
                (garage.getOccupied())));
    }
}
