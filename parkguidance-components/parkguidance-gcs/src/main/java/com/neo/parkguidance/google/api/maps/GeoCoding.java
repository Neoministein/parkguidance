package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.impl.http.HTTPRequestSender;
import com.neo.parkguidance.core.impl.http.HTTPResponse;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.http.HTTPRequest;
import com.neo.parkguidance.core.entity.StoredValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is used for calling the Google Cloud Platform GeoLocation service
 */
@Stateless
public class GeoCoding {

    public static final String TYPE = "geocoding";

    public static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/";
    public static final String ADDRESS = "address=";

    private static final Logger LOGGER = LogManager.getLogger(GeoCoding.class);

    @Inject
    StoredValueService storedValueService;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    HTTPRequestSender httpRequestSender = new HTTPRequestSender();

    /**
     * Find the coordinates associated with the address
     *
     * @param address the address to use
     */
    public void findCoordinates(Address address) {
        HTTPRequest httpRequest = new HTTPRequest();

        String query = GoogleConstants.addressQuery(address);

        elasticSearchProvider.save(GoogleConstants.ELASTIC_INDEX, GoogleConstants.elasticLog(TYPE, query));

        String url = API_URL + GoogleConstants.JSON + ADDRESS + query + GoogleConstants.KEY;

        httpRequest.setUrl(url + storedValueService.getString(StoredValue.V_GOOGLE_MAPS_API));
        httpRequest.setRequestMethod("GET");
        HTTPResponse httpResponse = httpRequestSender.call(httpRequest);

        switch (httpResponse.getCode()) {
        case HttpServletResponse.SC_OK:
            parseRequestStatus(new JSONObject(httpResponse.getBody()),address);
            break;
        case HttpServletResponse.SC_BAD_REQUEST:
            LOGGER.warn("HTTP ERROR 400 Bad request");
            throw new IllegalArgumentException(GoogleConstants.E_INVALID_ADDRESS);
        case HttpServletResponse.SC_NOT_FOUND:
            LOGGER.warn("HTTP ERROR 404 not found");
            throw new GoogleCloudServiceException(GoogleConstants.E_TRY_AGAIN);
        case -1:
            throw new GoogleCloudServiceException(GoogleConstants.E_INTERNAL_ERROR);
        default:
            LOGGER.warn("HTTP {} {}", httpResponse.getCode(), httpResponse.getBody());
            throw new GoogleCloudServiceException(GoogleConstants.E_EXTERNAL_ERROR + httpResponse.getCode());
        }
    }

    protected void parseRequestStatus(JSONObject jsonObject, Address address) {
        String status = jsonObject.getString("status");
        switch (status) {
        case "OK":
            parseCoordinates(jsonObject.getJSONArray("results"),address);
            break;
        case "ZERO_RESULTS":
        case "INVALID_REQUEST":
            LOGGER.info(GoogleConstants.E_GOOGLE_CLOUD_PLATFORM + "User has given invalid address {}", status, address);
            throw new IllegalArgumentException(GoogleConstants.E_INVALID_ADDRESS);
        case "UNKNOWN_ERROR":
            LOGGER.warn(GoogleConstants.E_GOOGLE_CLOUD_PLATFORM + " address {}", status, address);
            throw new GoogleCloudServiceException(GoogleConstants.E_TRY_AGAIN);
        case "OVER_DAILY_LIMIT":
        case "OVER_QUERY_LIMIT":
        case "REQUEST_DENIED":
        default:
            LOGGER.warn( GoogleConstants.E_GOOGLE_CLOUD_PLATFORM + " address {}", status, address);
            throw new GoogleCloudServiceException(GoogleConstants.E_EXTERNAL_ERROR + status);
        }
    }

    protected void parseCoordinates(JSONArray jsonArray,Address address) {
        JSONObject location = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        address.setLatitude(location.getDouble("lat"));
        address.setLongitude(location.getDouble("lng"));
    }
}
