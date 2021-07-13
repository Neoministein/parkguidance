package com.neo.parkguidance.elastic.impl;

import com.neo.parkguidance.elastic.api.ElasticSearchConnectionStatusEvent;
import com.neo.parkguidance.elastic.api.constants.ElasticSearchConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class ElasticSearchConnectionProvider implements Serializable {

    @Inject
    Event<ElasticSearchConnectionStatusEvent> connectionStatusEvent;

    private volatile RestHighLevelClient client;

    private AtomicBoolean connectorInitializationOngoing = new AtomicBoolean(false);

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
        throw new IllegalStateException("Elastic client is not yet ready");
    }

    protected synchronized void disconnect() {
        if (client == null) {
            return;
        }

        try {
            client.close();
        } catch (IOException e) {
            //TODO LOGGER
        } finally {
            client = null;
            connectorInitializationOngoing.set(false);
        }
    }

    protected synchronized void connect() {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(
                ElasticSearchConstants.DEFAULT_HOST_NAME,
                ElasticSearchConstants.DEFAULT_PORT,
                ElasticSearchConstants.DEFAULT_SCHEME)));
        connectionStatusEvent.fire(new ElasticSearchConnectionStatusEvent(ElasticSearchConnectionStatusEvent.STATUS_EVENT_CONNECTED));
    }
}
