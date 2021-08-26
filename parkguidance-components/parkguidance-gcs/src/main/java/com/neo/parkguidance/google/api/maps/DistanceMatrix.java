package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.impl.http.HTTPRequestSender;
import com.neo.parkguidance.core.impl.http.HTTPResponse;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.http.HTTPRequest;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.entity.StoredValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used for calling the Google Cloud Platform Distance Matrix service
 */
@Stateless
public class DistanceMatrix {

    public static final String TYPE = "DistanceMatrix";

    public static final String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
    public static final String ORIGIN = "origins=";
    public static final String DESTINATION = "&destinations=";

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceMatrix.class);

    @Inject
    StoredValueService storedValueService;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    HTTPRequestSender httpRequestSender = new HTTPRequestSender();

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, double latitude, double longitude) {
        return findDistance(parkingGarageList, new StringBuilder(latitude + "%2C" + longitude + DESTINATION));
    }

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, Address address) {
        return findDistance(parkingGarageList, new StringBuilder(GoogleConstants.addressQuery(address) + DESTINATION));
    }

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList , StringBuilder query) {
        String url = API_URL + GoogleConstants.JSON + ORIGIN;

        for (ParkingGarage parkingGarage : parkingGarageList) {
            query.append(GoogleConstants.addressQuery(parkingGarage.getAddress()));
            query.append("%7C");
        }
        String finalQuery = query.substring(0, query.length() - 3);
        url += finalQuery;

        elasticSearchProvider.save(GoogleConstants.ELASTIC_INDEX, GoogleConstants.elasticLog(TYPE,finalQuery));

        HTTPRequest apiRequest = new HTTPRequest();
        apiRequest.setUrl(url + GoogleConstants.KEY + storedValueService.getString(StoredValue.V_GOOGLE_MAPS_API));
        apiRequest.setRequestMethod("GET");
        HTTPResponse httpResponse = httpRequestSender.call(apiRequest);

        switch (httpResponse.getCode()) {
        case HttpServletResponse.SC_OK:
            return parseRequestStatus(new JSONObject(httpResponse.getBody()), parkingGarageList);
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

    protected List<DistanceDataObject> parseRequestStatus(JSONObject jsonObject, List<ParkingGarage> parkingGarageList) {
        String status = jsonObject.getString("status");
        switch (status) {
        case "OK":
            return parseDistance(jsonObject.getJSONArray("rows"),parkingGarageList);
        case "INVALID_REQUEST":
            LOGGER.warn(GoogleConstants.E_GOOGLE_CLOUD_PLATFORM, status);
            throw new IllegalArgumentException(GoogleConstants.E_INVALID_ADDRESS);
        case "UNKNOWN_ERROR":
            LOGGER.warn(GoogleConstants.E_GOOGLE_CLOUD_PLATFORM, status);
            throw new GoogleCloudServiceException(GoogleConstants.E_TRY_AGAIN);
        case "MAX_ELEMENTS_EXCEEDED":
        case "MAX_DIMENSIONS_EXCEEDED":
        case "OVER_DAILY_LIMIT":
        case "OVER_QUERY_LIMIT":
        case "REQUEST_DENIED":
        default:
            LOGGER.warn(GoogleConstants.E_GOOGLE_CLOUD_PLATFORM, status);
            throw new GoogleCloudServiceException(GoogleConstants.E_SYS_ADMIN + status);
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