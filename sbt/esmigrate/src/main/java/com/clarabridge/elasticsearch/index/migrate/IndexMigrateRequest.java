package com.clarabridge.elasticsearch.index.migrate;

import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.unit.ByteSizeUnit;
//import org.elasticsearch.common.unit.ByteSizeValue;

import java.util.Set;

public class IndexMigrateRequest {
    private long projectId;

    private int shards;
    private int batchSize;
    private int writeThreads;

    private Set<String> dvFields;

    private boolean obsolete;

    private TimeValue sleepBetweenBatches;
    private TimeValue scrollKeepAlive;


    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    public long getProjectId() {
        return projectId;
    }

    public void setShards(int shards) {
        this.shards = shards;
    }
    public int getShards() {
        return shards;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    public int getBatchSize() {
        return batchSize;
    }

    public void setWriteThreads(int writeThreads) {
        this.writeThreads = writeThreads;
    }
    public int getWriteThreads() {
        return writeThreads;
    }

    public void setDvFields(Set<String> dvFields) {
        this.dvFields = dvFields;
    }
    public Set<String> getDvFields() {
        return dvFields;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }
    public boolean getObsolete() {
        return obsolete;
    }

    public void setScrollKeepAlive(TimeValue scrollKeepAlive) {
        this.scrollKeepAlive = scrollKeepAlive;
    }
    public TimeValue getScrollKeepAlive() {
        return scrollKeepAlive;
    }

    public void setSleepBetweenBatches(TimeValue sleepBetweenBatches) {
        this.sleepBetweenBatches = sleepBetweenBatches;
    }
    public TimeValue getSleepBetweenBatches() {
        return sleepBetweenBatches;
    }
}
