package com.neo.parkguidance.core.api.external.google.maps;

import com.neo.parkguidance.core.api.HTTPRequestSender;
import com.neo.parkguidance.core.api.external.google.GoogleApi;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ApiRequest;
import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.dao.StoredValueEntityManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Stateless
public class GeoCoding {

    public static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/";
    public static final String ADDRESS = "address=";

    @Inject
    StoredValueEntityManager storedValueManager;

    @Inject
    HTTPRequestSender httpRequestSender;

    public void findCoordinates(Address address) throws RuntimeException{
        ApiRequest apiRequest = new ApiRequest();
        String url = API_URL + GoogleApi.JSON + ADDRESS + GoogleApi.addressQuery(address) + GoogleApi.KEY;

        apiRequest.setUrl(url);
        apiRequest.setRequestMethod("GET");
        httpRequestSender.call(apiRequest, storedValueManager.findValue(StoredValue.V_GOOGLE_MAPS_API).getValue());

        switch (apiRequest.getResponseCode()) {
        case HttpServletResponse.SC_OK:
            parseRequestStatus(new JSONObject(apiRequest.getResponseInput()),address);
            break;
        case HttpServletResponse.SC_BAD_REQUEST:
            throw new IllegalArgumentException(GoogleApi.E_INVALID_ADDRESS);
        case HttpServletResponse.SC_NOT_FOUND:
            throw new RuntimeException(GoogleApi.E_TRY_AGAIN);
        case -1:
            throw new RuntimeException(GoogleApi.E_INTERNAL_ERROR);
        default:
            throw new RuntimeException(GoogleApi.E_EXTERNAL_ERROR + apiRequest.getResponseCode());
        }
    }

    protected void parseRequestStatus(JSONObject jsonObject, Address address) throws RuntimeException{
        String status = jsonObject.getString("status");
        switch (status) {
        case "OK":
            parseCoordinates(jsonObject.getJSONArray("results"),address);
            break;
        case "ZERO_RESULTS":
        case "INVALID_REQUEST":
            throw new IllegalArgumentException(GoogleApi.E_INVALID_ADDRESS);
        case "UNKNOWN_ERROR":
            throw new RuntimeException(GoogleApi.E_TRY_AGAIN);
        case "OVER_DAILY_LIMIT":
        case "OVER_QUERY_LIMIT":
        case "REQUEST_DENIED":
        default:
            throw new RuntimeException(GoogleApi.E_EXTERNAL_ERROR + status);
        }
    }

    protected void parseCoordinates(JSONArray jsonArray,Address address) {
        JSONObject location = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        address.setLatitude(location.getDouble("lat"));
        address.setLongitude(location.getDouble("lng"));
    }
}
