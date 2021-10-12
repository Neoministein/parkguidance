package com.neo.parkguidance.elastic.impl;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import com.neo.parkguidance.elastic.api.ElasticSearchConnectionStatusEvent;
import com.neo.parkguidance.elastic.api.constants.ElasticSearchConstants;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for upholding the connection to the elasticsearch nodes.
 */
@ApplicationScoped
public class ElasticSearchConnectionProvider implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConnectionProvider.class);

    @Inject
    StoredValueService storedValueService;

    @Inject
    Event<ElasticSearchConnectionStatusEvent> connectionStatusEvent;

    private volatile RestHighLevelClient client;

    private List<String> nodeList = new ArrayList<>();

    private final AtomicBoolean connectorInitializationOngoing = new AtomicBoolean(false);

    @PostConstruct
    public void onStartUp() {
        LOGGER.debug("Loading elastic search configuration");
        String nodes = storedValueService.getString(ElasticSearchConstants.SEARCH_NODES_ADDRESS,
                ElasticSearchConstants.DEFAULT_HOST_NAME);
        nodeList.clear();
        nodeList.addAll(StringUtils.commaSeparatedStrToList(nodes));
        LOGGER.debug("Elasticsearch nodes {}", nodes);
    }

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
        if (client != null) {
            return;
        }

        try {
            List<HttpHost> nodes = getNodes();
            initializeClient(nodes);
        } finally {
            // end initialization process
            connectorInitializationOngoing.set(false);
        }
    }

    /**
     * Create Elasticsearch client based on one or more HttpHost nodes.
     *
     * @param nodes
     *            list of nodes
     */
    protected synchronized void initializeClient(List<HttpHost> nodes) {
        CredentialsProvider credentialsProvider = getCredentialsProvider();

        for (HttpHost node : nodes) {
            LOGGER.info("Elasticsearch configuration. Host:{} Port:{} Protocol: {}", node.getHostName(), node.getPort(),
                    node.getSchemeName());
        }

        if (credentialsProvider != null) {
            client = new RestHighLevelClient(RestClient.builder(nodes.toArray(new HttpHost[nodes.size()]))
                    .setHttpClientConfigCallback(
                            httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)));
        } else {
            client = new RestHighLevelClient(RestClient.builder(nodes.toArray(new HttpHost[nodes.size()])));
        }

        LOGGER.debug("Initializing elasticsearch complete");
        connectionStatusEvent.fire(new ElasticSearchConnectionStatusEvent(ElasticSearchConnectionStatusEvent.STATUS_EVENT_CONNECTED));
    }

    /**
     * Creates a CredentialsProvider in order to connect to the elastic search cluster if credentials are needed.
     * If no username or password is found in the {@link StoredValueService} null will be returned
     *
     * @return credentialsProvider
     */
    protected CredentialsProvider getCredentialsProvider() {
        String username = storedValueService.getString(ElasticSearchConstants.ELASTIC_SEARCH_USERNAME, null);
        String password = storedValueService.getString(ElasticSearchConstants.ELASTIC_SEARCH_PASSWORD, null);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return credentialsProvider;

    }

    protected List<HttpHost> getNodes() {
        try {
            List<HttpHost> nodes = new ArrayList<>(nodeList.size());
            for (String nodeUrl : nodeList) {
                nodes.add(HttpHost.create(nodeUrl));
            }
            return nodes;
        } catch (Exception e) {
            LOGGER.error("Failed load ElasticSearch nodes {} with exception: {}", nodeList, e);
            throw e;
        }
    }
}
