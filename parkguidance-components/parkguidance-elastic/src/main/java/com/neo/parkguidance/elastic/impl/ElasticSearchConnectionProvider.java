package com.neo.parkguidance.elastic.impl;

import com.neo.parkguidance.elastic.api.ElasticSearchConnectionStatusEvent;
import com.neo.parkguidance.elastic.api.constants.ElasticSearchConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for upholding the connection to the elasticsearch nodes.
 * //TODO implement a way to configure the URL of elasticsearch as well as of multiple nodes
 */
@ApplicationScoped
public class ElasticSearchConnectionProvider implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConnectionProvider.class);

    @Inject
    Event<ElasticSearchConnectionStatusEvent> connectionStatusEvent;

    private volatile RestHighLevelClient client;

    private final AtomicBoolean connectorInitializationOngoing = new AtomicBoolean(false);

    /**
     * Returns a connected client. If there isn't one yer one is created.
     */
    public RestHighLevelClient getClient() {
        RestHighLevelClient c = client;

        if (c != null) {
            return c;
        }

        if (connectorInitializationOngoing.compareAndSet(false, true)) {
            try {
                connect();
                return client;
            } catch (Exception e) {
                connectorInitializationOngoing.set(false);
            }
        }
        LOGGER.error("Elastic client is not yet ready yet");
        throw new IllegalStateException("Elastic client is not yet ready");
    }

    /**
     * Disconnects the client from elasticsearch
     */
    protected synchronized void disconnect() {
        if (client == null) {
            return;
        }

        try {
            client.close();
        } catch (IOException e) {
            LOGGER.warn("Unable to close connection to elasticsearch correctly. Setting client to null");
        } finally {
            client = null;
            connectorInitializationOngoing.set(false);
        }
    }

    /**
     * Connects the client to elasticsearch and fires a {@link ElasticSearchConnectionStatusEvent}
     */
    protected synchronized void connect() {
        LOGGER.debug("Initializing elasticsearch client");
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(
                ElasticSearchConstants.DEFAULT_HOST_NAME,
                ElasticSearchConstants.DEFAULT_PORT,
                ElasticSearchConstants.DEFAULT_SCHEME)));
        connectionStatusEvent.fire(new ElasticSearchConnectionStatusEvent(ElasticSearchConnectionStatusEvent.STATUS_EVENT_CONNECTED));
        LOGGER.debug("Initializing elasticsearch complete");
    }
}
