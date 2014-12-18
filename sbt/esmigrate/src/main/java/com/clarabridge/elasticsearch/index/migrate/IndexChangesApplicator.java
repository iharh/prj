package com.clarabridge.elasticsearch.index.migrate;

import com.clarabridge.elasticsearch.index.migrate.plugin.IndexChanges;

import org.elasticsearch.client.Client;

import org.elasticsearch.common.bytes.BytesReference;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.update.UpdateRequestBuilder;
//import org.elasticsearch.action.update.UpdateResponse;


import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexChangesApplicator implements IndexChangesListener, Closeable {
    private static final Logger log = LoggerFactory.getLogger(IndexChangesApplicator.class);

    private Client client;
    private IndexChanges changes;
    private String dstIndexName;

    public IndexChangesApplicator(Client client, IndexChanges changes, String dstIndexName) {
        this.client = client;
        this.changes = changes;
        this.dstIndexName = dstIndexName;
        changes.addListener(this);
    }

    @Override
    public void onChange(String id, long version, BytesReference srcRef) {
        // TODO: print srcRef using XContentHelper
        log.info("onChange index: {} id: {} ver: {}", dstIndexName, id, version);

        final String type = ESConstants.TYPE_DOCUMENT; // TODO: need to specify a type here

        try {

        final IndexRequestBuilder idxreqb = client.prepareIndex(dstIndexName, type, id)
            .setSource(srcRef)
            //.setRouting(routing)
            //.setRefresh(true);
        ;
        //final IndexRequest idxreq = idxreqb.request();

        //if (version <= 1) {
            //final IndexResponse rsp = idxreqb.get();
            //log.info("onChange index: {} id: {} ver: {} created: {}", dstIndexName, id, version, rsp.isCreated());
        //} else {
        //    final UpdateRequestBuilder updreqb = client.prepareUpdate(dstIndexName, type, id)
        //        .setDoc(idxreq)
        //        .setUpsert(idxreq)
        //    ;
        //    final UpdateResponse rsp = updreqb.get();
        //    log.info("onChange update: {} id: {} ver: {} created: {}", dstIndexName, id, version, rsp.isCreated());
        //}
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onDelete(String id, long version) {
        log.info("onDelete index: {} id: {} ver: {}", dstIndexName, id, version);
    }

    @Override
    public void close() {
        if (changes != null) {
            changes.removeListener();
        }
    }
}
