package com.clarabridge.elasticsearch.index.migrate;

import com.clarabridge.elasticsearch.index.migrate.plugin.IndexChanges;

import org.elasticsearch.client.Client;

import org.elasticsearch.common.bytes.BytesReference;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;


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
        //log.info("Changes {} found", (changes == null ? " not" : ""));
        changes.addListener(this);
    }

    //public IndexChangesApplicator startListen() {
    //    log.info("Changes {} found", (changes == null ? " not" : ""));
    //    return this;
    //}

    @Override
    public void onChange(String id, long version, BytesReference srcRef) {
        log.info("onChange index: {} id: {} ver: {}", dstIndexName, id, version);

        IndexRequestBuilder reqb = client.prepareIndex(dstIndexName, ESConstants.TYPE_DOCUMENT, id) // TODO: need to specify a type here
            .setSource(srcRef)
            //.setRouting(routing)
            .setRefresh(true);

        IndexResponse rsp = reqb.get();

        log.info("{}", rsp.isCreated());
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
