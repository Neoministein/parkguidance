package com.neo.parkguidance.elastic.impl.launcher;

import com.neo.parkguidance.core.api.HTTPRequest;
import com.neo.parkguidance.core.api.HTTPRequestSender;
import com.neo.parkguidance.core.api.HTTPResponse;
import com.neo.parkguidance.elastic.api.constants.ElasticSearchConstants;

public class ElasticLauncher {

    public static void main(String[] args) {
        createRawDataIndex();
        createGCSIndex();
    }

    private static void createRawDataIndex() {
        HTTPRequest httpRequest = new HTTPRequest();
        httpRequest.setUrl(ElasticSearchConstants.DEFAULT_URL + "/raw-parking-data");
        httpRequest.setRequestMethod(HTTPRequest.PUT);
        httpRequest.addRequestProperty("Content-Type","application/json");
        httpRequest.setRequestBody("{\"mappings\": {\"properties\": {\"timestamp\": {\"type\": \"date\"}}}}");

        HTTPResponse response = new HTTPRequestSender().call(httpRequest);

        System.out.println(response.getCode() + " " + response.getBody());
    }

    private static void createGCSIndex() {
        HTTPRequest httpRequest = new HTTPRequest();
        httpRequest.setUrl(ElasticSearchConstants.DEFAULT_URL + "/gcs");
        httpRequest.setRequestMethod(HTTPRequest.PUT);
        httpRequest.addRequestProperty("Content-Type","application/json");
        httpRequest.setRequestBody("{\"mappings\": {\"properties\": {\"timestamp\": {\"type\": \"date\"}}}}");

        HTTPResponse response = new HTTPRequestSender().call(httpRequest);

        System.out.println(response.getCode() + " " + response.getBody());
    }

}
