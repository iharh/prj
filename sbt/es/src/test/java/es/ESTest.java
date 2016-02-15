package es;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;


import org.elasticsearch.node.Node;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import org.elasticsearch.cluster.metadata.IndexMetaData;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilterBuilder;

import org.elasticsearch.index.shard.service.InternalIndexShard;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;

import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;


import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.stream.Stream;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESTest {
    private static final Logger log = LoggerFactory.getLogger(ESTest.class);

    private Node node;
    protected Client client;
    protected IndicesAdminClient iac;

    //private final static String clusterName = "epbygomw0024-5432-postgres-win_ss";
    private final static String clusterName = "localhost-1521-ihorcl-win_ss";
    //
    //final String clusterName = "elasticsearch";

    @Before
    public void setUp() {
        Settings settings = settingsBuilder()
            //.put("http.port", "9200")
            //.put("discovery.zen.ping.unicast.hosts", "localhost")
            .put("discovery.zen.ping.multicast.enabled", false)
            .put("cluster.name", clusterName)
            //.put("node.master", false)
            .build();

        //node = nodeBuilder()
        //    .clusterName(clusterName)
        //    .client(true)
        //    .settings(settings)
        //   .node(); // data(false).

        client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        assertNotNull(client);

        //client = node.client();
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
    
    private final static long clusterId = 1;
    private final static long projectId = 8459;

    @Test
    public void testAgg() throws Exception {
        final String readAliasName = "read_" + clusterId + "$" + projectId;
        assertTrue(iac.prepareAliasesExist(readAliasName).get().exists());

	final String aggregationName = "detectedFeaturesAggregation"; //$NON-NLS-1$
        final String [] fields = new String [] { "cb_bc_brand", "cb_bc_product" };

        final FiltersAggregationBuilder aggregations = AggregationBuilders.filters(aggregationName);

        Stream.of(fields).forEach(field -> aggregations.filter(field, FilterBuilders.existsFilter(field)));

        SearchRequestBuilder reqB = client.prepareSearch(readAliasName)
            .setTypes("sentence")
            .setSearchType(SearchType.COUNT) // SearchType.DFS_QUERY_THEN_FETCH
	    .addAggregation(aggregations)
	    .setSize(0);

        SearchResponse resp = reqB.execute().actionGet();
	    				
	Filters agg = resp.getAggregations().get(aggregationName);

        Stream.of(fields).forEach(field -> {
            log.info("field: {}", field);
            log.info("sentence count: {}", agg.getBucketByKey(field).getDocCount());
        });
    }
}

