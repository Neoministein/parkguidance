package com.neo.parkguidance.rest.api.v1.parkgrage;

import com.neo.parkguidance.core.api.auth.AuthenticationService;
import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.core.impl.validation.AddressValidator;
import com.neo.parkguidance.core.impl.validation.EntityValidationException;
import com.neo.parkguidance.core.impl.validation.ParkingGarageValidator;
import com.neo.parkguidance.google.api.maps.GeoCoding;
import com.neo.parkguidance.google.api.maps.GoogleCloudServiceException;
import com.neo.parkguidance.rest.api.InternalRestException;
import org.json.JSONArray;
import org.json.JSONObject;

@Stateless
public class ParkingGarageRestFacade {

    public static final String STORED_VALUE_PERMISSION = "rest.parkingGarage.requiredPermissionForAuth";
    private static final String INVALID_KEY = "Invalid key";

    @Inject
    AuthenticationService authenticationService;

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    EntityDao<Address> addressDao;

    @Inject
    ParkingGarageValidator parkingGarageValidator;

    @Inject AddressValidator addressValidator;

    @Inject
    GeoCoding geoCoding;

    public List<ParkingGarage> getAll() {
        return this.parkingGarageDao.findAll();
    }

    public List<ParkingGarage> getLike(ParkingGarage example) {
        return this.parkingGarageDao.findLikeExample(example);
    }

    public ParkingGarage getKey(String key) throws InternalRestException {
        ParkingGarage parkingGarage = this.parkingGarageDao.find(key);
        if (parkingGarage == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, INVALID_KEY);
        }
        return parkingGarage;
    }

    public ParkingGarage getKey(String key, String token) throws InternalRestException {
        if (key == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, INVALID_KEY);
        }

        this.authorizedUser(token);
        ParkingGarage parkingGarage = parkingGarageDao.find(key);
        if (parkingGarage == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, INVALID_KEY);
        }

        return parkingGarage;
    }

    public ParkingGarage getAccessKey(String accessKey) throws InternalRestException {
        ParkingGarage parkingGarage = this.authenticationService.authenticateGarage(accessKey);
        if (parkingGarage == null) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid Accesskey");
        }

        return parkingGarage;
    }

    public ParkingGarage revokeAccessKey(ParkingGarage parkingGarage) {
        parkingGarageValidator.invalidateAccessKey(parkingGarage);
        parkingGarageDao.edit(parkingGarage);
        return parkingGarage;
    }

    public void createGarage(ParkingGarage parkingGarage) throws InternalRestException, EntityValidationException {
        parkingGarageValidator.validatePrimaryKey(parkingGarage.getPrimaryKey());
        findCoordinates(parkingGarage.getAddress());
        addressDao.edit(parkingGarage.getAddress());
        parkingGarageDao.create(parkingGarage);
    }

    public void updateGarage(ParkingGarage parkingGarage) throws InternalRestException {
        if (!addressValidator.hasAddressChanged(parkingGarage.getAddress())) {
            findCoordinates(parkingGarage.getAddress());
            addressDao.edit(parkingGarage.getAddress());
        }

        parkingGarageDao.edit(parkingGarage);
    }

    protected void findCoordinates(Address address) throws InternalRestException {
        try {
            geoCoding.findCoordinates(address);
        } catch (IllegalArgumentException ex) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (GoogleCloudServiceException ex) {
            throw new InternalRestException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    public void checkForMissingValues(ParkingGarage parkingGarage) throws InternalRestException {
        List<String> missingParameters = new ArrayList<>();

        if (StringUtils.isEmpty(parkingGarage.getKey())) {
            missingParameters.add("key");
        }

        if (StringUtils.isEmpty(parkingGarage.getName())) {
            missingParameters.add("name");
        }
        if (parkingGarage.getSpaces() == 0) {
            missingParameters.add("spaces");
        }
        if (StringUtils.isEmpty(parkingGarage.getAddress().getCityName())) {
            missingParameters.add("cityName");
        }
        if (parkingGarage.getAddress().getPlz() == null) {
            missingParameters.add("plz");
        }
        if (StringUtils.isEmpty(parkingGarage.getAddress().getStreet())) {
            missingParameters.add("street");
        }

        if (!missingParameters.isEmpty()) {
            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters:" + missingParameters.toString());
        }
    }

    public JSONArray parseParkingGarageToJSONArray(List<ParkingGarage> parkingGarageList, boolean authRequest) {
        JSONArray jsonArray = new JSONArray();
        for (ParkingGarage parkingGarage: parkingGarageList) {
            jsonArray.put(parseParkingGarageToJSONObject(parkingGarage, authRequest));
        }
        return jsonArray;
    }

    protected JSONObject parseParkingGarageToJSONObject(ParkingGarage parkingGarage, boolean authRequest) {
        JSONObject jsonGarage = new JSONObject();
        JSONObject jsonAddress = new JSONObject();
        jsonGarage.put("key", parkingGarage.getKey());
        jsonGarage.put("name", parkingGarage.getName());
        jsonGarage.put("spaces", parkingGarage.getSpaces());
        jsonGarage.put("occupied", parkingGarage.getOccupied());
        jsonGarage.put("description", StringUtils.parseToEmptyString(parkingGarage.getDescription()));
        jsonGarage.put("price", StringUtils.parseToEmptyString(parkingGarage.getPrice()));
        jsonGarage.put("operator", StringUtils.parseToEmptyString(parkingGarage.getOperator()));
        jsonAddress.put("city_name", parkingGarage.getAddress().getCityName());
        jsonAddress.put("plz", parkingGarage.getAddress().getPlz());
        jsonAddress.put("street", parkingGarage.getAddress().getStreet());
        jsonAddress.put("number", StringUtils.parseToEmptyString(parkingGarage.getAddress().getNumber()));
        if (authRequest) {
            jsonGarage.put("accessKey", parkingGarage.getAccessKey());
            jsonAddress.put("longitude", parkingGarage.getAddress().getLongitude());
            jsonAddress.put("latitude", parkingGarage.getAddress().getLatitude());
        }

        jsonGarage.put("address", jsonAddress);
        return jsonGarage;
    }

    public void authorizedUser(String token) throws InternalRestException {
        if (this.authenticationService.authenticateUser(token,
                authenticationService.getRequiredPermissions(STORED_VALUE_PERMISSION)) == null) {
            throw new InternalRestException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid User token");
        }
    }
}