package com.clarabridge.elasticsearch.index.migrate.plugin;

import com.clarabridge.elasticsearch.index.migrate.IndexChangesListener;

import org.elasticsearch.common.bytes.BytesReference;

import org.elasticsearch.index.indexing.IndexingOperationListener;

import org.elasticsearch.index.engine.Engine.Create;
import org.elasticsearch.index.engine.Engine.Delete;
import org.elasticsearch.index.engine.Engine.Index;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

//import java.util.concurrent.atomic.AtomicInteger;

public class IndexChanges extends IndexingOperationListener {
    private static final ESLogger LOG = Loggers.getLogger(IndexChanges.class);

    //public enum ChangeType { INDEX, CREATE, DELETE; }

    private String indexName;

    //private AtomicInteger shardCount;

    private volatile IndexChangesListener listener; // only one migrate at a time is supported

    public IndexChanges(String indexName) {
        this.indexName = indexName;
    }
/*
    public int addShard() {
        return shardCount.incrementAndGet();
    }
    
    public int removeShard() {
        return shardCount.decrementAndGet();
    }
*/
    public void addListener(IndexChangesListener listener) {
        // TODO: assert this.listener == null
        this.listener = listener;
        LOG.info("start listening for changes of index: {}", indexName);
    }

    public void removeListener() {
        this.listener = null;
        LOG.info("stop listening for changes of index: {}", indexName);
    }

    private void addChange(String id, long version, String type, BytesReference srcRef) {
        if (listener != null) {
            listener.onChange(id, version, type, srcRef);
        }
    }

    private void addDelete(String id, long version, String type) {
        if (listener != null) {
            listener.onDelete(id, version, type);
        }
    }

    @Override
    public void postCreate(Create create) {
        addChange(create.id(), create.version(), create.type(), create.source());
    }

    @Override
    public void postIndex(Index index) {
        addChange(index.id(), index.version(), index.type(), index.source());
    }

    @Override
    public void postDelete(Delete delete) {
        addDelete(delete.id(), delete.version(), delete.type());
    }
}
