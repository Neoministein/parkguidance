package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.core.api.HTTPRequestSender;
import com.neo.parkguidance.core.api.HTTPResponse;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.api.HTTPRequest;
import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.dao.StoredValueEntityManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Stateless
public class GeoCoding {

    public static final String TYPE = "geocoding";

    public static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/";
    public static final String ADDRESS = "address=";

    @Inject
    StoredValueEntityManager storedValueManager;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    HTTPRequestSender httpRequestSender = new HTTPRequestSender();

    public void findCoordinates(Address address) throws RuntimeException{
        HTTPRequest httpRequest = new HTTPRequest();

        String query = "";

        elasticSearchProvider.save(GoogleConstants.ELASTIC_INDEX, GoogleConstants.elasticLog(TYPE, query));

        String url = API_URL + GoogleConstants.JSON + ADDRESS + GoogleConstants.addressQuery(address) + GoogleConstants.KEY;

        httpRequest.setUrl(url + storedValueManager.findValue(StoredValue.V_GOOGLE_MAPS_API).getValue());
        httpRequest.setRequestMethod("GET");
        HTTPResponse httpResponse = httpRequestSender.call(httpRequest);

        switch (httpResponse.getCode()) {
        case HttpServletResponse.SC_OK:
            parseRequestStatus(new JSONObject(httpResponse.getBody()),address);
            break;
        case HttpServletResponse.SC_BAD_REQUEST:
            throw new IllegalArgumentException(GoogleConstants.E_INVALID_ADDRESS);
        case HttpServletResponse.SC_NOT_FOUND:
            throw new RuntimeException(GoogleConstants.E_TRY_AGAIN);
        case -1:
            throw new RuntimeException(GoogleConstants.E_INTERNAL_ERROR);
        default:
            throw new RuntimeException(GoogleConstants.E_EXTERNAL_ERROR + httpResponse.getCode());
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
            throw new IllegalArgumentException(GoogleConstants.E_INVALID_ADDRESS);
        case "UNKNOWN_ERROR":
            throw new RuntimeException(GoogleConstants.E_TRY_AGAIN);
        case "OVER_DAILY_LIMIT":
        case "OVER_QUERY_LIMIT":
        case "REQUEST_DENIED":
        default:
            throw new RuntimeException(GoogleConstants.E_EXTERNAL_ERROR + status);
        }
    }

    protected void parseCoordinates(JSONArray jsonArray,Address address) {
        JSONObject location = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        address.setLatitude(location.getDouble("lat"));
        address.setLongitude(location.getDouble("lng"));
    }
}
