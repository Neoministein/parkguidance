package com.neo.parkguidance.rest.api.v1.parkdata;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.auth.AuthenticationService;
import com.neo.parkguidance.core.api.dao.AbstractEntityDao;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.parkdata.impl.service.ParkDataService;
import com.neo.parkguidance.rest.api.InternalRestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Stateless
public class ParkingDataRestFacade {

    private static final Logger LOGGER = LogManager.getLogger(ParkingDataRestFacade.class);

    public static final String STORED_VALUE_PERMISSION = "rest.parkdata.requiredPermissionForAuth";

    @Inject
    ParkDataService parkDataService;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public JSONObject getSingleData(String key, Integer time) throws InternalRestException {
        JSONObject rootObject = new JSONObject();
        rootObject.put("count", getTimeList(key).get(time));
        return rootObject;
    }

    public List<Integer> getTimeList(String key) throws InternalRestException {
        List<Integer> parkData = parkDataService.getParkData(key);

        if (parkData == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "Key ["+key+"] does not exist");
        }
        return parkData;
    }

    public ParkingGarage getAccessKey(String accessKey) throws InternalRestException {
        ParkingGarage parkingGarage = this.authenticationService.authenticateGarage(accessKey);
        if (parkingGarage == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid AccessKey");
        }

        return parkingGarage;
    }

    public ParkingGarage getKey(String key, String token) throws InternalRestException {
        this.authorizedUser(token);
        ParkingGarage parkingGarage = parkingGarageDao.find(key);
        if (parkingGarage == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid key");
        }

        return parkingGarage;
    }

    public synchronized void updateParkingData(ParkingGarage parkingGarage, String type, Integer count)
            throws InternalRestException {
        switch (type) {
        case "incr":
            setOccupied(parkingGarage, offsetOccupied(parkingGarage, 1));
            break;
        case "decr":
            setOccupied(parkingGarage, offsetOccupied(parkingGarage,-1));
            break;
        case "set":
            setOccupied(parkingGarage, count);
            break;
        default:
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "Type: [" + type + "] is not specified");
        }
    }

    protected int offsetOccupied(ParkingGarage garage, int offset) {
        return garage.getOccupied() + offset;
    }

    public void setOccupied(ParkingGarage garage, int amount) {
        garage.setOccupied(amount);

        parkingGarageDao.edit(garage);

        try {
            elasticSearchProvider.save("raw-parking-data", getJSONContent(garage));
        }catch (Exception e) {
            LOGGER.warn("");
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

    public void authorizedUser(String token) throws InternalRestException {
        if (this.authenticationService.authenticateUser(token,
                authenticationService.getRequiredPermissions(STORED_VALUE_PERMISSION)) == null) {
            throw new InternalRestException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid User token");
        }
    }
}
