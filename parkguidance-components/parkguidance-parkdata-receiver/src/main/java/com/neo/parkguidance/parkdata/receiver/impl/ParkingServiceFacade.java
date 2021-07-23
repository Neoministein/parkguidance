package com.neo.parkguidance.parkdata.receiver.impl;

import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.parkdata.receiver.impl.security.ParkingGarageAuthentication;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * This class handles updates to {@link ParkingGarage} via the REST endpoint {@link com.neo.parkguidance.parkdata.receiver.api.servlet.ParkingService}
 */
@Stateless
public class ParkingServiceFacade {

    private static final int INCREASE = 1;
    private static final int DECREASE = -1;
    private static final String AMOUNT = "amount";

    private static final Logger LOGGER = LogManager.getLogger(ParkingServiceFacade.class);

    @Inject
    ParkingGarageAuthentication authentication;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public synchronized int updateParkingData(String requestString) {
        try {
            JSONObject requestData = new JSONObject(requestString);
            String accessKey = requestData.getString(ParkingGarage.C_ACCESS_KEY);
            ParkingGarage parkingGarage = authentication.validate(accessKey);

            if(parkingGarage == null) {
                LOGGER.info("Illegal access unknown accessKey [{}] ", accessKey);
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
            elasticSearchProvider.save("raw-parking-data", getJSONContent(garage));
        }catch (Exception e) {
            LOGGER.warn("Couldn't save the data to elasticsearch {}", e.getMessage());
            //TODO PUT ON QUE
        }
    }

    protected String getJSONContent(ParkingGarage parkingGarage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("garage", parkingGarage.getKey());
        jsonObject.put("timestamp",new Date().getTime());
        jsonObject.put("occupied", parkingGarage.getOccupied());
        jsonObject.put("sorted", false);

        return jsonObject.toString();
    }
}
