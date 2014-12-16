package com.clarabridge.elasticsearch.index.migrate.plugin;

import org.elasticsearch.common.bytes.BytesReference;

public interface IndexChangesListener {
    public void onChange(String id, long version, BytesReference srcRef);
    public void onDelete(String id, long version);
}
