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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        String url = API_URL + GoogleApi.JSON + ADDRESS + defineQuery(address) + GoogleApi.KEY;

        apiRequest.setUrl(url);
        apiRequest.setRequestMethod("GET");
        httpRequestSender.call(apiRequest, storedValueManager.findValue(StoredValue.V_GOOGLE_MAPS_API).getValue());

        switch (apiRequest.getResponseCode()) {
        case HttpServletResponse.SC_OK:
            parseRequestStatus(new JSONObject(apiRequest.getResponseInput()),address);
            break;
        case HttpServletResponse.SC_BAD_REQUEST:
            throw new IllegalArgumentException("This doesn't seem to be a valid address");
        case HttpServletResponse.SC_NOT_FOUND:
            throw new RuntimeException("Try agin later");
        case -1:
            throw new RuntimeException("Internal server error");
        default:
            throw new RuntimeException("External Server error:" + apiRequest.getResponseCode());
        }
    }

    protected String defineQuery(Address address) throws RuntimeException{
        String query =  address.getStreet() + "+" +
                address.getNumber() + "+" +
                address.getCityName() + "+" +
                address.getPlz();

        try {
            return URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException(StandardCharsets.UTF_8.toString() + " is not supported");
        }
    }

    protected void parseRequestStatus(JSONObject jsonObject, Address address) throws RuntimeException{
        String status = jsonObject.getString("status");
        switch (jsonObject.getString("status")) {
        case "OK":
            parseCoordinates(jsonObject.getJSONArray("results"),address);
            break;
        case "ZERO_RESULTS":
        case "INVALID_REQUEST":
            throw new IllegalArgumentException("This doesn't seem to be a valid address");
        case "UNKNOWN_ERROR":
            throw new RuntimeException("Please try again in a couple of minutes");
        case "OVER_DAILY_LIMIT":
        case "OVER_QUERY_LIMIT":
        case "REQUEST_DENIED":
        default:
            throw new RuntimeException("Please contact a system administrator: " + status);
        }
    }

    protected void parseCoordinates(JSONArray jsonArray,Address address) {
        JSONObject location = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        address.setLatitude(location.getDouble("lat"));
        address.setLongitude(location.getDouble("lng"));
    }
}
