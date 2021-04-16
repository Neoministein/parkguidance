package com.neo.parkguidance.core.api.external.google.maps;

import com.neo.parkguidance.core.api.HTTPRequestSender;
import com.neo.parkguidance.core.api.external.google.GoogleApi;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ApiRequest;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.dao.StoredValueEntityManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class DistanceMatrix {

    public static final String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
    public static final String ORIGIN = "origins=";
    public static final String DESTINATION = "&destinations=";

    @Inject
    StoredValueEntityManager storedValueManager;

    @Inject
    HTTPRequestSender httpRequestSender;

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, double latitude, double longitude) {
        return findDistance(parkingGarageList, new StringBuilder(latitude + "%2C" + longitude + DESTINATION));
    }

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, Address address) {
        return findDistance(parkingGarageList, new StringBuilder(GoogleApi.addressQuery(address) + DESTINATION));
    }

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList , StringBuilder query) {
        String url = API_URL + GoogleApi.JSON + ORIGIN;

        for (ParkingGarage parkingGarage : parkingGarageList) {
            query.append(GoogleApi.addressQuery(parkingGarage.getAddress()));
            query.append("%7C");
        }

        url += query.substring(0, query.length() - 3);

        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setUrl(url + GoogleApi.KEY);
        apiRequest.setRequestMethod("GET");
        httpRequestSender.call(apiRequest, storedValueManager.findValue(StoredValue.V_GOOGLE_MAPS_API).getValue());

        switch (apiRequest.getResponseCode()) {
        case HttpServletResponse.SC_OK:
            return   parseRequestStatus(new JSONObject(apiRequest.getResponseInput()), parkingGarageList);
        case HttpServletResponse.SC_BAD_REQUEST:
            throw new IllegalArgumentException(GoogleApi.E_UNVALID_ADDRESS);
        case HttpServletResponse.SC_NOT_FOUND:
            throw new RuntimeException(GoogleApi.E_TRY_AGAIN);
        case -1:
            throw new RuntimeException(GoogleApi.E_INTERNAL_ERROR);
        default:
            throw new RuntimeException(GoogleApi.E_EXTERNAL_ERROR + apiRequest.getResponseCode());
        }
    }

    protected List<DistanceDataObject> parseRequestStatus(JSONObject jsonObject, List<ParkingGarage> parkingGarageList) throws RuntimeException {
        String status = jsonObject.getString("status");
        switch (status) {
        case "OK":
            return parseDistance(jsonObject.getJSONArray("rows"),parkingGarageList);
        case "INVALID_REQUEST":
            throw new IllegalArgumentException(GoogleApi.E_UNVALID_ADDRESS);
        case "UNKNOWN_ERROR":
            throw new RuntimeException(GoogleApi.E_TRY_AGAIN);
        case "MAX_ELEMENTS_EXCEEDED":
        case "MAX_DIMENSIONS_EXCEEDED":
        case "OVER_DAILY_LIMIT":
        case "OVER_QUERY_LIMIT":
        case "REQUEST_DENIED":
        default:
            throw new RuntimeException(GoogleApi.E_SYS_ADMIN + status);
        }
    }

    protected List<DistanceDataObject> parseDistance(JSONArray jsonArray, List<ParkingGarage> parkingGarageList) {
        JSONArray elements = jsonArray.getJSONObject(0).getJSONArray("elements");
        List<DistanceDataObject> dataObjectList = new ArrayList<>();

        for (int i = 0; i < elements.length(); i++) {
            JSONObject currentObject = elements.getJSONObject(i);
            JSONObject distance = currentObject.getJSONObject("distance");
            JSONObject duration = currentObject.getJSONObject("duration");
            DistanceDataObject dataObject = new DistanceDataObject();

            dataObject.setDistanceInt(distance.getInt("value"));
            dataObject.setDistanceString(distance.getString("text"));
            dataObject.setDurationInt(duration.getInt("value"));
            dataObject.setDurationString(duration.getString("text"));

            dataObject.setParkingGarage(parkingGarageList.get(i));

            dataObjectList.add(dataObject);
        }
        Collections.sort(dataObjectList);

        return dataObjectList;
    }

}
