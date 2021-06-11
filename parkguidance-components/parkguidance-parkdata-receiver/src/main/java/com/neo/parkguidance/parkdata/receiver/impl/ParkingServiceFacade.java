package com.neo.parkguidance.parkdata.receiver.impl;

import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchClientProvider;
import com.neo.parkguidance.parkdata.receiver.impl.security.ParkingGarageAuthentication;
import com.neo.parkguidance.core.entity.ParkingGarage;
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
    private static final String AMOUNT = "amount";

    @Inject
    ParkingGarageAuthentication authentication;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    @Inject
    ElasticSearchClientProvider elasticSearchClientProvider;

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
                    setOccupied(parkingGarage, offsetOccupied(parkingGarage, INCREASE));
                    break;
                case "decr":
                    setOccupied(parkingGarage, offsetOccupied(parkingGarage,DECREASE));
                    break;
                case "set":
                    setOccupied(parkingGarage, requestData.getInt(AMOUNT));
                    break;
                default:
                    return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
                }
                return HttpServletResponse.SC_OK;
            }
        }catch (JSONException ex) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
    }

    protected int offsetOccupied(ParkingGarage garage, int offset) {
        return garage.getOccupied() + offset;
    }

    public void setOccupied(ParkingGarage garage, int amount) {
        garage.setOccupied(amount);

        parkingGarageManager.edit(garage);

        try {
            elasticSearchClientProvider.save("raw-parking-data", getJSONContent(garage));
        }catch (Exception e) {
            //TODO PUT ON QUE
        }
    }

    protected String getJSONContent(ParkingGarage parkingGarage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("garage", parkingGarage.getId());
        jsonObject.put("timestamp",new Date().getTime());
        jsonObject.put("occupied", parkingGarage.getOccupied());

        return jsonObject.toString();
    }
}
