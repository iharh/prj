package com.clarabridge.elasticsearch.index.migrate.plugin;

import org.elasticsearch.index.indexing.IndexingOperationListener;

import org.elasticsearch.index.engine.Engine.Create;
import org.elasticsearch.index.engine.Engine.Delete;
import org.elasticsearch.index.engine.Engine.Index;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

//import java.util.concurrent.atomic.AtomicInteger;

public class IndexChanges extends IndexingOperationListener {
    private static final ESLogger LOG = Loggers.getLogger(IndexChanges.class);

    public enum ChangeType { INDEX, CREATE, DELETE; }

    private String indexName;

    //private AtomicInteger shardCount;

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
    private void addChange(ChangeType changeType, String id, long version) {
        //lastChange=c.timestamp;
        //changes.add(c);
        //triggerWatchers(c);
        LOG.info("index: " + indexName + " op: " + changeType.toString() + " version: " + version);
    }

    @Override
    public void postCreate(Create create) {
        addChange(ChangeType.CREATE, create.id(), create.version());
    }

    @Override
    public void postDelete(Delete delete) {
        addChange(ChangeType.DELETE, delete.id(), delete.version());
    }

    @Override
    public void postIndex(Index index) {
        addChange(ChangeType.INDEX, index.id(), index.version());
    }
}
