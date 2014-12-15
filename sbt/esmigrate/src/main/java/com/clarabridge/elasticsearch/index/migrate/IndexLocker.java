package com.clarabridge.elasticsearch.index.migrate;

import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

class IndexLocker implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(IndexLocker.class);

    // return SETTING_READ_ONLY after fixing:
    //   https://github.com/elasticsearch/elasticsearch/issues/2833
    private static final String SETTING_TO_LOCK = IndexMetaData.SETTING_BLOCKS_WRITE;

    private IndicesAdminClient iac;
    private String indexName;
    private volatile boolean closed; // = false is by default

    public IndexLocker(IndicesAdminClient iac, String indexName) {
        this.iac = iac;
        this.indexName = indexName;
        refresh();
        boolean ack = iac.prepareUpdateSettings(indexName)
            .setSettings(settingsBuilder().put(SETTING_TO_LOCK, true))
            .get().isAcknowledged();
        log.info("index {} changed to RO: {}", indexName, Boolean.toString(ack));
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        unlock();
    }

    private void refresh() {
        iac.prepareRefresh(indexName).get();
        log.info("index {} refresh called", indexName);
    }

    private void unlock() {
        if (iac.prepareExists(indexName).get().isExists()) {
            boolean ack = false;
            try {
                ack = iac.prepareUpdateSettings(indexName)
                    .setSettings(settingsBuilder().put(SETTING_TO_LOCK, false))
                    .get().isAcknowledged();
                refresh();
            } catch (Exception e) {
                log.error("Index unlocking error: ", e);
            }
            log.info("index {} changed to RW: {}", indexName, Boolean.toString(ack));
        } else {
            log.warn("index {} does not exist - probably has already been deleted by user externally", indexName);
        }
    }
}
