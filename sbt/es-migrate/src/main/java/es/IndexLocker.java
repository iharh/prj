package es;

import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

class IndexLocker implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(IndexLocker.class);

    IndicesAdminClient iac;
    private String indexName;
    private volatile boolean closed; // = false is by default

    public IndexLocker(IndicesAdminClient iac, String indexName) {
        this.iac = iac;
        this.indexName = indexName;
        refresh();
        boolean ack = iac.prepareUpdateSettings(indexName)
            .setSettings(settingsBuilder().put(IndexMetaData.SETTING_READ_ONLY, true))
            .get().isAcknowledged();
        log.info("index {} chanted to RO: {}", indexName, Boolean.toString(ack));
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
        boolean ack = iac.prepareUpdateSettings(indexName)
            .setSettings(settingsBuilder().put(IndexMetaData.SETTING_READ_ONLY, false))
            .get().isAcknowledged();
        log.info("index {} chanted to RW: {}", indexName, Boolean.toString(ack));
        refresh();
    }
}
