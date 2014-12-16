package com.clarabridge.elasticsearch.index.migrate.plugin;

import com.clarabridge.elasticsearch.index.migrate.IndexNameFinder;
import com.clarabridge.elasticsearch.index.migrate.IndexMigrator;
import com.clarabridge.elasticsearch.index.migrate.IndexMigrateRequest;

import org.elasticsearch.ExceptionsHelper;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.TimeValue;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.indices.IndicesLifecycle.Listener;

import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;


import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.BytesRestResponse;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.OK;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

public class MigrateRestHandler implements RestHandler {
    private static final ESLogger LOG = Loggers.getLogger(MigrateRestHandler.class);

    private final IndicesService indicesService;
    private final Client client;
    private final IndicesAdminClient iac;

    private Map<String, IndexChanges> changes;

    private final IndexNameFinder inf;

    @Inject
    public MigrateRestHandler(RestController restController, IndicesService indicesService, Client client) {
        restController.registerHandler(POST, "/_migrate", this); // $NON-NLS-1$

        this.indicesService = indicesService;
        this.client = client;
        this.iac = client.admin().indices();

        this.changes = new ConcurrentHashMap<String, IndexChanges>();

        this.inf = new IndexNameFinder(iac);

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
        final String projectIdStr = request.param("projectId"); // $NON-NLS-1$
        final long projectId = Long.parseLong(projectIdStr);

        // indicesService.indexServiceSafe(String index) throws IndexMissingException;

        //final IndexService indexService = indicesService.indexService(projectIdStr); // indicesService.hasIndex(...)
        //String whoSafe = indexService.getClass().getName();

        final String indexName = inf.findCur(projectIdStr);

        IndexMigrateRequest req = new IndexMigrateRequest();
        req.setProjectId(projectId);

        req.setShards(5);
        req.setWriteThreads(4);
        req.setBatchSize(10000);
        req.setDvFields(new HashSet<String>());
        req.setScrollKeepAlive(TimeValue.timeValueMinutes(30));
        //req.setSleepBetweenBatches(sleepBetweenBatches);

        try {
            LOG.info("migrate called as non-locked for project: " + projectIdStr);

            IndexMigrator im = new IndexMigrator(client);

            im.migrateIndex(req, changes.get(indexName));
        } catch (Throwable e) {
            LOG.error("Cannot process migrate request", e);
            channel.sendResponse(new BytesRestResponse(ExceptionsHelper.status(e)));
        }

        //channel.sendResponse(new BytesRestResponse(OK, "Hello, " + whoSafe)); // $NON-NLS-1$
        channel.sendResponse(new BytesRestResponse(OK)); // $NON-NLS-1$
    }
}
