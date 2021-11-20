package com.neo.parkguidance.elastic.impl.launcher;

import com.neo.parkguidance.framework.impl.http.HTTPRequest;
import com.neo.parkguidance.framework.impl.http.HTTPRequestSender;
import com.neo.parkguidance.framework.impl.http.HTTPResponse;
import com.neo.parkguidance.elastic.api.constants.ElasticSearchConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A launcher for creating the necessary indexes in elastic search indexes
 */
public class ElasticLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticLauncher.class);

    public static void main(String[] args) {
        createRawDataIndex();
        createGCSIndex();
        createSortedParkingData();
    }

    private static void createRawDataIndex() {
        HTTPRequest httpRequest = createRequest("/raw-parking-data");
        httpRequest.setRequestBody("{\"mappings\":{\"properties\":{"
                + "\"garage\":{\"type\":\"text\"}," + ""
                + "\"occupied\":{\"type\":\"integer\"},"
                + "\"timestamp\":{\"type\":\"date\"},"
                + "\"sorted\":{\"type\":\"boolean\"}}}}");

        HTTPResponse response = new HTTPRequestSender().call(httpRequest);

        logResponse(response);
    }

    private static void createSortedParkingData() {
        HTTPRequest httpRequest = createRequest("/sorted-parking-data");
        httpRequest.setRequestBody("{\"mappings\": {\"properties\": {"
                + "\"garage\": {\"type\": \"text\"},"
                + "\"halfHour\": {\"type\": \"byte\"},"
                + "\"day\": {\"type\": \"byte\"},"
                + "\"week\": {\"type\": \"byte\"},"
                + "\"year\": {\"type\": \"integer\"},"
                + "\"occupied\": {\"type\": \"integer\"}}}}");

        HTTPResponse response = new HTTPRequestSender().call(httpRequest);

        logResponse(response);
    }

    private static void createGCSIndex() {
        HTTPRequest httpRequest = createRequest("/gcs");
        httpRequest.setRequestBody("{\"mappings\":{\"properties\":{"
                + "\"timestamp\":{\"type\":\"date\"},"
                + "\"distanceDataObjects\":{\"type\":\"nested\"}"
                + "}}}");

        HTTPResponse response = new HTTPRequestSender().call(httpRequest);

        logResponse(response);
    }

    private static HTTPRequest createRequest(String index) {
        HTTPRequest httpRequest = new HTTPRequest();
        httpRequest.setUrl(ElasticSearchConstants.DEFAULT_URL + index);
        httpRequest.setRequestMethod(HTTPRequest.PUT);
        httpRequest.addRequestProperty("Content-Type","application/json");
        return httpRequest;
    }

    private static void logResponse(HTTPResponse response) {
        LOGGER.info("{} | {}",response.getCode(), response.getBody());
    }

}
