package com.clarabridge.elasticsearch.index.migrate;

import com.clarabridge.elasticsearch.index.migrate.plugin.IndexChanges;

import org.elasticsearch.client.Client;

import org.elasticsearch.common.bytes.BytesReference;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.update.UpdateRequestBuilder;
//import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
//import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;


import java.io.Closeable;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexChangesApplicator implements IndexChangesListener, Closeable {
    private static final Logger log = LoggerFactory.getLogger(IndexChangesApplicator.class);

    private Client client;
    private IndexChanges changes;
    private String dstIndexName;

    private ConcurrentMap<String, String> postponedDeletes;

    public IndexChangesApplicator(Client client, IndexChanges changes, String dstIndexName) {
        this.client = client;
        this.changes = changes;
        this.dstIndexName = dstIndexName;

        postponedDeletes = new ConcurrentHashMap<String, String>();

        changes.addListener(this);
    }

    @Override
    public void onChange(String id, long version, String type, BytesReference srcRef) {
        // TODO: print srcRef using XContentHelper
        log.info("onChange index: {} id: {} ver: {}, type: {}", dstIndexName, id, version, type);

        try {
            final IndexRequestBuilder idxreqb = client.prepareIndex(dstIndexName, type, id)
                .setSource(srcRef)
                //.setRouting(routing)
                //.setRefresh(true);
            ;
            //final IndexRequest idxreq = idxreqb.request();

            //if (version <= 1) {
                final IndexResponse rsp = idxreqb.get();

                log.info("onChange index: {} id: {} ver: {} created: {}", dstIndexName, id, version, rsp.isCreated());

                postponedDeletes.remove(id, type); // there can be the same ids for multiple types
            //} else {
            //    final UpdateRequestBuilder updreqb = client.prepareUpdate(dstIndexName, type, id)
            //        .setDoc(idxreq)
            //        .setUpsert(idxreq)
            //    ;
            //    final UpdateResponse rsp = updreqb.get();
            //    log.info("onChange update: {} id: {} ver: {} created: {}", dstIndexName, id, version, rsp.isCreated());
            //}
        } catch (Throwable e) {
            // TODO: communicate the error to migration process
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onDelete(String id, long version, String type) {
        log.info("onDelete index: {} id: {} ver: {}, type: {}", dstIndexName, id, version, type);

        try {
            postponedDeletes.put(id, type);

            final DeleteRequestBuilder delreqb = client.prepareDelete(dstIndexName, type, id)
                //.setRouting(routing)
            ;

            final DeleteResponse rsp = delreqb.get();
            final boolean found = rsp.isFound();
            log.info("onDelete index: {} id: {} ver: {} found: {}", dstIndexName, id, version, found);

            if (found) { // this is just to eliminate races
                postponedDeletes.remove(id, type);
            }

        } catch (Throwable e) {
            // TODO: communicate the error to migration process
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        if (changes != null) {
            changes.removeListener();
        }
        deletePostponed();
    }

    private void deletePostponed() {
        synchronized(postponedDeletes) {
            for (Map.Entry<String, String> e : postponedDeletes.entrySet()) {
                String id = e.getKey();
                String type = e.getValue();

                final DeleteRequestBuilder delreqb = client.prepareDelete(dstIndexName, type, id)
                    //.setRouting(routing)
                ;

                final DeleteResponse rsp = delreqb.get();
                final boolean found = rsp.isFound();
                log.info("deletePostponed index: {} id: {} found: {}", dstIndexName, id, found);
            }
            postponedDeletes.clear();
        }
    }
}
