package com.neo.parkguidance.elastic.api.constants;

/**
 * Constants for the elastic search implementation in parkguidance
 */
public interface ElasticSearchConstants {

    String ELASTIC_SEARCH_CONFIG_MAP = "com.neo.parkguidance.elastic";

    String ELASTIC_SEARCH_USERNAME = "com.neo.parkguidance.elastic.username";
    String ELASTIC_SEARCH_PASSWORD = "com.neo.parkguidance.elastic.password";

    String SEARCH_NODES_ADDRESS = "com.neo.parkguidance.elastic.nodes";

    String DEFAULT_SCHEME = "http";
    String LOCALHOST_HOST_NAME = "127.0.0.1";
    int DEFAULT_PORT = 9200;

    String DEFAULT_URL = DEFAULT_SCHEME + "://" + LOCALHOST_HOST_NAME + ":" + DEFAULT_PORT;
}
