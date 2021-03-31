package com.neo.parkguidance.parkdata.receiver.impl;

import com.neo.parkguidance.parkdata.receiver.impl.security.ParkingGarageAuthentication;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Stateless
public class ParkingServiceFacade {

    private static final Object lock = new Object();

    private static final int INCREASE = 1;
    private static final int DECREASE = -1;

    @Inject
    private ParkingGarageAuthentication authentication;

    @Inject
    private ParkingDataEntityManager parkingDataManager;

    public int updateParkingData(String requestString) {
        try {
            JSONObject requestData = new JSONObject(requestString);
            ParkingGarage parkingGarage = authentication.validate(requestData.getString(ParkingGarage.C_ACCESS_KEY));

            if(parkingGarage == null) {
                return HttpServletResponse.SC_FORBIDDEN;
            }
            synchronized (lock) {
                switch (requestData.getString("type")) {
                case "incr":
                    parkingDataManager.create(parkingGarage,INCREASE);
                    break;
                case "decr":
                    parkingDataManager.create(parkingGarage, DECREASE);
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
}
