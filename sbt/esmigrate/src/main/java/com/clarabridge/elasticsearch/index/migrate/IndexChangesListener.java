package com.clarabridge.elasticsearch.index.migrate;

import org.elasticsearch.common.bytes.BytesReference;

public interface IndexChangesListener {
    public void onChange(String id, long version, String type, BytesReference srcRef);
    public void onDelete(String id, long version, String type);
}
