package com.neo.parkguidance.elastic.api;

public class ElasticSearchConnectionStatusEvent {

    public static final String STATUS_EVENT_CONNECTED = "Connected";
    public static final String STATUS_EVENT_DISCONNECT = "Disconnect";

    String connectionStatus;


    public ElasticSearchConnectionStatusEvent(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

}
