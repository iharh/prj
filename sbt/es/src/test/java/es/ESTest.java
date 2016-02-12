package es;

import org.junit.Test;
import org.junit.Ignore;


import org.elasticsearch.node.Node;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import org.elasticsearch.cluster.metadata.IndexMetaData;

import org.elasticsearch.index.shard.service.InternalIndexShard;


import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESTest {
    private static final Logger log = LoggerFactory.getLogger(ESTest.class);

    private Node node;
    protected Client client;
    protected IndicesAdminClient iac;

    //private final static String clusterName = "epbygomw0024-5432-postgres-win_ss";
    private final static String clusterName = "epbygomw0024-5432-oracle-win_ss";
    //
    //final String clusterName = "elasticsearch";

    @Before
    public void setUp() {
        Settings settings = settingsBuilder()
            //.put("http.port", "9200")
            .put("discovery.zen.ping.unicast.hosts", "localhost")
            .put("discovery.zen.ping.multicast.enabled", false)
            .put("node.master", false)
            .build();

        node = nodeBuilder()
            .clusterName(clusterName)
            .client(true)
            .settings(settings)
            .node(); // data(false).

        client = node.client();
        iac = client.admin().indices();
    }

    @After
    public void tearDown() {
        try {
            if (node != null) {
                node.close();
            }
        } catch (Throwable t) {
        }
    }
    
    private final static long projectId = 1404;

    @Ignore
    public void testAlias() throws Exception {
        log.info("es");

        final String readAliasName = "read_" + projectId;
        assertTrue(iac.prepareAliasesExist(readAliasName).get().exists());
    }
}

