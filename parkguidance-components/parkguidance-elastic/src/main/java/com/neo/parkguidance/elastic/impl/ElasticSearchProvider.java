package com.neo.parkguidance.elastic.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.elastic.api.ElasticSearchConnectionStatusEvent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * This class provides methods to interact with elastic search
 */
@ApplicationScoped
public class ElasticSearchProvider {

    private static final Logger LOGGER = LogManager.getLogger(ElasticSearchProvider.class);

    @Inject
    ElasticSearchConnectionProvider connection;

    private volatile BulkProcessor bulkProcessor;

    protected synchronized void setupBulkProcessor() {
        if (bulkProcessor != null) {
            return;
        }

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest bulkRequest) {
                int numberOfActions = bulkRequest.numberOfActions();
                LOGGER.debug("Executing bulk [{}] with {} requests, first doc [{}]", executionId, numberOfActions,
                            numberOfActions > 0 ? bulkRequest.requests().get(0) : "Empty");

            }

            @Override
            public void afterBulk(long executionId, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                LOGGER.info("Executed bulk [{}] with {} requests, hasFailures: {}, took: {}, ingestTook: {}",
                        executionId, bulkRequest.numberOfActions(), bulkResponse.hasFailures(), bulkResponse.getTook(),
                        bulkResponse.getIngestTook());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest bulkRequest, Throwable failure) {
                LOGGER.info("Executed bulk [{}] with failure complete bulk will be retried, message: {}", executionId,
                        failure.getMessage());
            }
        };

        try {
            BulkProcessor.Builder builder = createBulkRequestBuilder(listener);
            bulkProcessor = builder.build();
        } catch (IllegalStateException e) {
            LOGGER.error("Unable to create bulk processor", e);
        }
    }

    protected void connectionStatusListener(@Observes ElasticSearchConnectionStatusEvent event) {
        if (ElasticSearchConnectionStatusEvent.STATUS_EVENT_CONNECTED.equals(event.getConnectionStatus())) {
            setupBulkProcessor();
        }
    }

    protected BulkProcessor getBulkProcessor() {
        if (bulkProcessor == null) {
            getClient();
            throw new IllegalStateException("elasticsearch bulkProcessor not ready yet!");
        }
        return bulkProcessor;
    }

    protected BulkProcessor.Builder createBulkRequestBuilder(BulkProcessor.Listener listener) {
        BulkProcessor.Builder builder = BulkProcessor.builder(
                (request, bulkListener) ->
                        getClient().bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener);
        builder.setFlushInterval(TimeValue.timeValueSeconds(5));
        builder.setBulkActions(500);

        return builder;

    }

    protected RestHighLevelClient getClient() {
        return connection.getClient();
    }

    /**
     * Saves a {@link DataBaseEntity} to a elastic search index
     *
     * @param index the index the content is saved to
     * @param content the DataBaseEntity object needed to be saved
     */
    public void save(String index, DataBaseEntity<? extends DataBaseEntity<?>> content) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            save(index, mapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            LOGGER.error("Couldn't parse the database entity {} {}",
                    content.getClass().getName(),
                    content.getPrimaryKey());
        }
    }

    /**
     * Saves the content to elastic search
     *
     * @param index the index the content is saved to
     * @param content the content being saved
     */
    public void save(String index, String content) {
        getBulkProcessor().add(indexRequest(index,content));
    }

    /**
     * Sends a low level request to elastic search
     *
     * @param requestMethod the HTTP method
     * @param endpoint the endpoint at elasticsearch
     * @param jsonBody the body of the request
     *
     * @return the response from ElasticSearch
     * @throws IOException if a request couldn't go through properly
     */
    public String sendLowLevelRequest(String requestMethod, String endpoint, String jsonBody) throws IOException {
        Request request = new Request(requestMethod, endpoint);
        request.setJsonEntity(jsonBody);

        Response response = getClient().getLowLevelClient().performRequest(request);
        InputStream input = response.getEntity().getContent();
        return IOUtils.toString(input, Charset.defaultCharset());
    }

    private IndexRequest indexRequest(String index, String content) {
        final byte[] bytes = content.getBytes();
        final IndexRequest request = new IndexRequest(index);

        request.source(bytes, XContentType.JSON);
        return request;
    }
}