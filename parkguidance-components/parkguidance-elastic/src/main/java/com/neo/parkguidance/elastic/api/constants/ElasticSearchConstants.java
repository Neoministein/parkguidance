package com.neo.parkguidance.elastic.api.constants;

/**
 * Constants for the elastic search implementation in parkguidance
 */
public interface ElasticSearchConstants {

    String DEFAULT_SCHEME = "http";
    String DEFAULT_HOST_NAME = "127.0.0.1";
    int DEFAULT_PORT = 9200;

    String DEFAULT_URL = DEFAULT_SCHEME + "://" + DEFAULT_HOST_NAME + ":" + DEFAULT_PORT;
}
