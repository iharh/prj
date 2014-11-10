package com.clarabridge.elasticsearch.ingex.migrate.cli;

import com.clarabridge.elasticsearch.ingex.migrate.IndexMigrator;

import org.elasticsearch.node.Node;

import org.elasticsearch.common.settings.Settings;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class IndexMigratorCLI {
    private static final String O_PRJ       = "prj";       //$NON-NLS-1$
    private static final String O_CLUSTER   = "cluster";   //$NON-NLS-1$
    private static final String O_NODE      = "node";      //$NON-NLS-1$
    private static final String O_SHARDS    = "shards";    //$NON-NLS-1$
    private static final String O_THREADS   = "threads";   //$NON-NLS-1$
    private static final String O_BATCHSIZE = "batchsize"; //$NON-NLS-1$

    public static void main(String[] args) {
        Options opts = new Options();
        opts.addOption("p", O_PRJ      , true, "Project id"); //$NON-NLS-1$
        opts.addOption("c", O_CLUSTER  , true, "Cluster name"); //$NON-NLS-1$
        opts.addOption("n", O_NODE     , true, "IP/host the node to connect. Optional, default is localhost"); //$NON-NLS-1$
        //opts.addOption("m", false, "Copy mapping.");
        opts.addOption("s", O_SHARDS   , true, "Number of shards in new index. Optional, default 7"); //$NON-NLS-1$
        opts.addOption("t", O_THREADS  , true, "Number of writer threads. Optional, default 4");  //$NON-NLS-1$
        opts.addOption("b", O_BATCHSIZE, true, "Number of ES documents in the batch for indexing. Optional, default 10000"); //$NON-NLS-1$

        CommandLineParser parser = new GnuParser();
        Node node = null;
        try {
            CommandLine line = parser.parse(opts, args);
                            
            String projectIdStr = line.getOptionValue(O_PRJ);
            String clusterName = line.getOptionValue(O_CLUSTER);

            if (projectIdStr == null || clusterName == null) {
                System.err.println("Incomplete list of parameters"); //$NON-NLS-1$
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("IndexMigratorCLI", opts); //$NON-NLS-1$
                System.exit(1);	        	
            }

            long projectId = Long.decode(projectIdStr);

            String nodeStr = line.getOptionValue(O_NODE);
            if (nodeStr == null) {
                nodeStr = "localhost"; //$NON-NLS-1$
            }

            //boolean mapping = line.hasOption("m");
            
            String shardsStr = line.getOptionValue(O_SHARDS);
            int shards = shardsStr == null ? 7 : Integer.decode(shardsStr);
            
            String threadsStr = line.getOptionValue(O_THREADS);
            int threads = threadsStr == null ? 4 : Integer.decode(threadsStr);
            
            String batchsizeStr = line.getOptionValue(O_BATCHSIZE);
            int batchsize = batchsizeStr == null ? 10000 : Integer.decode(batchsizeStr);
            
            Settings settings = settingsBuilder()
                //.put("http.port", "9200")
                .put("discovery.zen.ping.unicast.hosts", nodeStr) //$NON-NLS-1$
                .put("discovery.zen.ping.multicast.enabled", false) //$NON-NLS-1$
                .put("node.master", false) //$NON-NLS-1$
                .build();
            
            node = nodeBuilder()
                .clusterName(clusterName)
                .client(true)
                .settings(settings)
                .node(); // data(false).

            IndexMigrator im = new IndexMigrator(node.client());
            im.migrateIndex(projectId, shards, batchsize, threads, null, false);
        }
        catch (ParseException e) {
            System.err.println("Options parsing failed: " + e.getMessage()); //$NON-NLS-1$
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage()); //$NON-NLS-1$
            e.printStackTrace();
        } finally {
            try {
                if (node != null) {
                    node.close();
                }
            } catch (Throwable t) {
            }
        }
    }
}
