package com.neo.parkguidance.elastic.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.elastic.api.ElasticSearchConnectionStatusEvent;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ElasticSearchClientProvider {

    @Inject ElasticSearchConnectionProvider connection;

    private volatile BulkProcessor bulkProcessor;

    protected synchronized void setupBulkProcessor() {
        if (bulkProcessor != null) {
            return;
        }

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest bulkRequest) {
                //TODO LOGGER
            }

            @Override
            public void afterBulk(long executionId, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                //TODO LOGGER
            }

            @Override
            public void afterBulk(long executionId, BulkRequest bulkRequest, Throwable failure) {
                //TODO LOGGER
            }
        };

        try {
            BulkProcessor.Builder builder = createBulkRequestBuilder(listener);
            bulkProcessor = builder.build();
        } catch (IllegalStateException e) {
            //TODO LOGGER
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

    public void save(String index, DataBaseEntity content) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            save(index, mapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            //TODO WRITE LOGGER
        }
    }
    public void save(String index, String content) {
        getBulkProcessor().add(indexRequest(index,content));
    }

    private IndexRequest indexRequest(String index, String content) {
        final byte[] bytes = content.getBytes();
        final IndexRequest request = new IndexRequest(index);

        request.source(bytes, XContentType.JSON);
        return request;
    }
}
