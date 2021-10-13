package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.core.api.geomap.GeoCodingService;
import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.impl.http.HTTPRequestSender;
import com.neo.parkguidance.core.impl.http.HTTPResponse;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.http.HTTPRequest;
import com.neo.parkguidance.core.entity.ConfigValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * This class is used for calling the Google Cloud Platform GeoLocation service
 */
@Stateless
public class GcsGeoCodingService implements GeoCodingService {

    public static final String TYPE = "geocoding";

    public static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/";
    public static final String ADDRESS = "address=";

    private static final Logger LOGGER = LogManager.getLogger(GcsGeoCodingService.class);

    @Inject ConfigService configService;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    HTTPRequestSender httpRequestSender = new HTTPRequestSender();

    /**
     * Find the coordinates associated with the address
     *
     * @param address the address to use
     */
    public void findCoordinates(Address address) {
        if (checkInCache(address)) {
            return;
        }
        HTTPRequest httpRequest = new HTTPRequest();

        String query = GoogleConstants.addressQuery(address);

        String url = API_URL + GoogleConstants.JSON + ADDRESS + query + GoogleConstants.KEY;

        httpRequest.setUrl(url + configService.getString(ConfigValue.V_GOOGLE_MAPS_API));
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
        elasticSearchProvider.save(GoogleConstants.ELASTIC_INDEX, createElasticDocument(address).toString());
    }

    @Override
    public void findAddress(Address address) {
        //TODO
        throw new RuntimeException("Not Impl Yet");
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

    protected JSONObject createElasticDocument(Address address) {
        if (address.getNumber() == null) {
            address.setNumber(-1);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TYPE);
        jsonObject.put("timestamp",new Date().getTime());
        jsonObject.put(Address.C_CITY_NAME, address.getCityName());
        jsonObject.put(Address.C_STREET, address.getStreet());
        jsonObject.put(Address.C_NUMBER, address.getNumber());
        jsonObject.put(Address.C_LONGITUDE, address.getLongitude());
        jsonObject.put(Address.C_LATITUDE, address.getLatitude());
        return jsonObject;
    }

    protected boolean checkInCache(Address address) {
        if (address.getNumber() == null) {
            address.setNumber(-1);
        }
        String jsonBody = "{"
                + "\"query\":{"
                + "\"bool\":{"
                + "\"must\":["
                + "{\"match\":{\"type\":\"geocoding\"}},"
                + "{\"match\":{\"city_name\":\"" + address.getCityName() +"\"}},"
                + "{\"match\":{\"street\":\"" + address.getStreet() + "\"}},"
                + "{\"match\":{\"number\":" + address.getNumber() + "}}"
                + "]"
                + "}"
                + "}"
                + "}";
        try {
            String result = elasticSearchProvider.sendLowLevelRequest("POST","/gcs/_search",jsonBody);
            JSONObject root = new JSONObject(result);
            JSONArray jsonArray = root.getJSONObject("hits").getJSONArray("hits");
            if (jsonArray.isEmpty()) {
                return false;
            }
            JSONObject data = jsonArray.getJSONObject(0).getJSONObject("_source");
            address.setLongitude(data.getDouble(Address.C_LONGITUDE));
            address.setLatitude(data.getDouble(Address.C_LATITUDE));
            return true;
        } catch (IOException e) {
            LOGGER.error("Error querying Elasticsearch", e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Unknown Elasticsearch error", e);
            return false;
        }
    }
}
