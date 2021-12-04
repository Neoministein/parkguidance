package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.framework.api.geomap.DistanceMatrixService;
import com.neo.parkguidance.framework.api.config.ConfigService;
import com.neo.parkguidance.framework.impl.geomap.DistanceDataObject;
import com.neo.parkguidance.framework.impl.http.HTTPRequestSender;
import com.neo.parkguidance.framework.impl.http.HTTPResponse;
import com.neo.parkguidance.framework.impl.utils.ConfigValueUtils;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.impl.http.HTTPRequest;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.entity.ConfigValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * This class is used for calling the Google Cloud Platform Distance Matrix service
 */
@Stateless
public class GcsDistanceMatrixService implements DistanceMatrixService {

    public static final String TYPE = "DistanceMatrix";

    public static final String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
    public static final String ORIGIN = "origins=";
    public static final String DESTINATION = "&destinations=";

    private static final String DISTANCE_MATRIX_CACHE_LIFESPAN = "com.neo.parkguidance.gcs.maps.distance-matrix.cache-lifespan";
    private static final int DEFAULT_CACHE_LIFESPAN = 1000 * 60 * 60 * 24 * 14;

    private static final Logger LOGGER = LoggerFactory.getLogger(GcsDistanceMatrixService.class);

    @Inject
    ConfigService configService;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    protected Map<String, ConfigValue> configValueMap = null;

    @PostConstruct
    public void init() {
        configValueMap = configService.getConfigMap("com.neo.parkguidance.gcs");
    }

    protected HTTPRequestSender httpRequestSender = new HTTPRequestSender();

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, double latitude, double longitude) {
        List<DistanceDataObject> distanceDataObjectList = checkInCache(latitude, longitude, parkingGarageList);
        if (!distanceDataObjectList.isEmpty()) {
            return distanceDataObjectList;
        }
        distanceDataObjectList = findDistance(parkingGarageList, new StringBuilder(latitude + "%2C" + longitude + DESTINATION));
        elasticSearchProvider.save(GoogleConstants.ELASTIC_INDEX, createElasticDocument(latitude,longitude,distanceDataObjectList).toString());
        return distanceDataObjectList;
    }

    public List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList, Address address) {
        List<DistanceDataObject> distanceDataObjectList = checkInCache(address, parkingGarageList);
        if (!distanceDataObjectList.isEmpty()) {
            return distanceDataObjectList;
        }
        distanceDataObjectList = findDistance(parkingGarageList, new StringBuilder(GoogleConstants.addressQuery(address) + DESTINATION));
        elasticSearchProvider.save(GoogleConstants.ELASTIC_INDEX, createElasticDocument(address,distanceDataObjectList).toString());
        return distanceDataObjectList;
    }

    protected List<DistanceDataObject> findDistance(List<ParkingGarage> parkingGarageList , StringBuilder query) {
        String url = API_URL + GoogleConstants.JSON + ORIGIN;

        for (ParkingGarage parkingGarage : parkingGarageList) {
            query.append(GoogleConstants.addressQuery(parkingGarage.getAddress()));
            query.append("%7C");
        }
        String finalQuery = query.substring(0, query.length() - 3);
        url += finalQuery;

        HTTPRequest apiRequest = new HTTPRequest();
        apiRequest.setUrl(url + GoogleConstants.KEY + ConfigValueUtils.parseString(configValueMap.get(ConfigValue.V_GOOGLE_MAPS_API)));
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

    protected JSONObject createRootDocument(List<DistanceDataObject> distanceDataObjectList, String type) {
        JSONObject root = new JSONObject();
        root.put("type",TYPE + type);
        root.put("timestamp",new Date().getTime());

        JSONArray distanceDataObjects = new JSONArray();
        for (DistanceDataObject distanceDataObject: distanceDataObjectList) {
            JSONObject distanceDataJSONObject = new JSONObject();
            distanceDataJSONObject.put("distanceInt", distanceDataObject.getDistanceInt());
            distanceDataJSONObject.put("distanceString",distanceDataObject.getDistanceString());
            distanceDataJSONObject.put("durationInt",distanceDataObject.getDurationInt());
            distanceDataJSONObject.put("durationString",distanceDataObject.getDurationString());
            distanceDataJSONObject.put(ParkingGarage.C_KEY, distanceDataObject.getParkingGarage().getKey());

            distanceDataObjects.put(distanceDataJSONObject);
        }
        root.put("distanceDataObjects",distanceDataObjects);
        return root;
    }

    protected JSONObject createElasticDocument(Address address, List<DistanceDataObject> distanceDataObjectList) {
        JSONObject root = createRootDocument(distanceDataObjectList, "-address");

        if (address.getHouseNumber() == null) {
            address.setHouseNumber(-1);
        }
        root.put(Address.C_CITY_NAME, address.getCityName());
        root.put(Address.C_STREET, address.getStreet());
        root.put(Address.C_HOUSE_NUMBER, address.getHouseNumber());
        root.put(Address.C_LONGITUDE, address.getLongitude());
        root.put(Address.C_LATITUDE, address.getLatitude());
        return root;
    }

    protected JSONObject createElasticDocument(double latitude, double longitude, List<DistanceDataObject> distanceDataObjectList) {
        JSONObject root = createRootDocument(distanceDataObjectList, "-latlng");

        root.put(Address.C_LONGITUDE, longitude);
        root.put(Address.C_LATITUDE, latitude);
        return root;
    }

    protected List<DistanceDataObject> checkInCache(double latitude, double longitude, List<ParkingGarage> parkingGarageList) {
        String jsonBody = "{"
                + "\"query\":{"
                + "\"bool\":{"
                + "\"must\":["
                + "{\"match\":{\"type\":\"" + TYPE + "\"}},"
                + "{\"match\":{\""+Address.C_LATITUDE+"\":\"" + latitude + "\"}},"
                + "{\"match\":{\""+Address.C_LONGITUDE+"\":\"" + longitude + "\"}}"
                + "]"
                + "}"
                + "}"
                + "}";
        return checkInCache(jsonBody, parkingGarageList);
    }

    protected List<DistanceDataObject> checkInCache(Address address, List<ParkingGarage> parkingGarageList) {
        if (address.getHouseNumber() == null) {
            address.setHouseNumber(-1);
        }
        String jsonBody = "{"
                + "\"query\":{"
                + "\"bool\":{"
                + "\"must\":["
                + "{\"match\":{\"type\":\"" + TYPE + "\"}},"
                + "{\"match\":{\"city_name\":\"" + address.getCityName() + "\"}},"
                + "{\"match\":{\"street\":\"" + address.getStreet() + "\"}},"
                + "{\"match\":{\"number\":" + address.getHouseNumber() + "}}"
                + "]"
                + "}"
                + "}"
                + "}";
        return checkInCache(jsonBody, parkingGarageList);

    }

    protected List<DistanceDataObject> checkInCache(String jsonBody, List<ParkingGarage> parkingGarageList) {
        try {
            String result = elasticSearchProvider.sendLowLevelRequest("POST", "/gcs/_search", jsonBody);
            JSONObject root = new JSONObject(result);
            JSONArray jsonArray = root.getJSONObject("hits").getJSONArray("hits");
            if (jsonArray.isEmpty()) {
                return Collections.emptyList();
            }
            JSONObject hit = jsonArray.getJSONObject(0);
            JSONObject source = hit.getJSONObject("_source");
            if (isStillValid(source)) {
                List<DistanceDataObject> distanceDataObjectList = createObjectsFromJSONObject(source, parkingGarageList);
                if (!distanceDataObjectList.isEmpty()) {
                    return distanceDataObjectList;
                }
            }
            invalidateCacheDocument(hit.getString("_id"));
        } catch (IOException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.error("Unknown Elasticsearch error", e);

        }
        return Collections.emptyList();
    }

    protected boolean isStillValid(JSONObject source) {
        return new Date().getTime() <= source.getLong("timestamp") +
                ConfigValueUtils.parseLong(configValueMap.get(DISTANCE_MATRIX_CACHE_LIFESPAN), DEFAULT_CACHE_LIFESPAN);
    }

    protected void invalidateCacheDocument(String id) {
        elasticSearchProvider.delete("gcs", id);
    }

    protected List<DistanceDataObject> createObjectsFromJSONObject(JSONObject source, List<ParkingGarage> parkingGarageList) {
        List<DistanceDataObject> distanceDataObjectList = new ArrayList<>();
        JSONArray distanceDataJSONArray = source.getJSONArray("distanceDataObjects");
        for (int i = 0; i < distanceDataJSONArray.length(); i++) {
            JSONObject distanceDataJSONObject = distanceDataJSONArray.getJSONObject(i);
            ParkingGarage currentParkingGarage = parkingGarageList.stream().filter(o -> o.getKey().equals(distanceDataJSONObject.get(ParkingGarage.C_KEY))).findFirst().orElse(null);

            if (currentParkingGarage == null) {
                return Collections.emptyList();
            }

            DistanceDataObject distanceDataObject = new DistanceDataObject();
            distanceDataObject.setParkingGarage(currentParkingGarage);
            distanceDataObject.setDistanceInt(distanceDataJSONObject.getInt("distanceInt"));
            distanceDataObject.setDistanceString(distanceDataJSONObject.getString("distanceString"));
            distanceDataObject.setDurationInt(distanceDataJSONObject.getInt("durationInt"));
            distanceDataObject.setDurationString(distanceDataJSONObject.getString("durationString"));
            distanceDataObjectList.add(distanceDataObject);
        }
        return distanceDataObjectList;
    }
}