package com.clarabridge.elasticsearch.index.migrate.plugin;

import org.elasticsearch.common.inject.Inject;

import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.BytesRestResponse;

import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.indices.IndicesLifecycle.Listener;

import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;


import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MigrateRestHandler implements RestHandler {
    private IndicesService indicesService;
    private Map<String, IndexChanges> changes;

    @Inject
    public MigrateRestHandler(RestController restController, IndicesService indicesService) {
        restController.registerHandler(GET, "/_hello", this); // $NON-NLS-1$

        this.indicesService = indicesService;
        this.changes = new ConcurrentHashMap<String, IndexChanges>();

        registerLifecycleHandler();
    }

    private void registerLifecycleHandler() {
        indicesService.indicesLifecycle().addListener(new Listener() {
            @Override
            public void afterIndexShardStarted(IndexShard indexShard) {
                if (indexShard.routingEntry().primary()) {
                    final String indexName = indexShard.shardId().index().name();

                    IndexChanges indexChanges;
                    synchronized (changes) {
                        indexChanges = changes.get(indexName);
                        if (indexChanges == null) {
                            indexChanges = new IndexChanges(indexName);
                            changes.put(indexName, indexChanges);
                        }
                    }
                    //indexChanges.addShard();
                    indexShard.indexingService().addListener(indexChanges);
                }
            }
        });
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        String who = request.param("who"); // $NON-NLS-1$
        //String whoSafe = (who != null) ? who : "world"; // $NON-NLS-1$

        final long projectId = 1404;
        // indicesService.indexServiceSafe(String index) throws IndexMissingException;
        final IndexService indexService = indicesService.indexService(Long.toString(projectId)); // indicesService.hasIndex(...)

        String whoSafe = indexService.getClass().getName();

        channel.sendResponse(new BytesRestResponse(OK, "Hello, " + whoSafe)); // $NON-NLS-1$
    }
}
