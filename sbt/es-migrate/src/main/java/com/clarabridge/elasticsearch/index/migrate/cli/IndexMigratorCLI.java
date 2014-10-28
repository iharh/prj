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
    public static void main(String[] args) {
        Options opts = new Options();
        opts.addOption("p", "prj", true, "Project id");
        opts.addOption("c", "cluster", true, "Cluster name");
        opts.addOption("n", "node", true, "IP/host the node to connect. Optional, default is localhost");
        //opts.addOption("m", false, "Copy mapping.");
        opts.addOption("s", "shards", true, "Number of shards in new index. Optional, default 5");
        opts.addOption("t", "threads", true, "Number of writer threads. Optional, default 4");
        opts.addOption("b", "batchsize", true, "Number of ES documents in the batch for indexing. Optional, default 10000");

        CommandLineParser parser = new GnuParser();
        Node node = null;
        try {
            CommandLine line = parser.parse(opts, args);
                            
            String projectIdStr = line.getOptionValue("prj");
            String clusterName = line.getOptionValue("cluster");

            if (projectIdStr == null || clusterName == null) {
                System.err.println("Incomplete list of parameters");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("IndexMigratorCLI", opts);
                System.exit(1);	        	
            }

            long projectId = Long.decode(projectIdStr);

            String nodeStr = line.getOptionValue("node");
            if (nodeStr == null) {
                nodeStr = "localhost";
            }

            //boolean mapping = line.hasOption("m");
            
            String shardsStr = line.getOptionValue("shards");
            int shards = shardsStr == null ? 5 : Integer.decode(shardsStr);
            
            String threadsStr = line.getOptionValue("threads");
            int threads = threadsStr == null ? 4 : Integer.decode(threadsStr);
            
            String batchsizeStr = line.getOptionValue("batchsize");
            int batchsize = batchsizeStr == null ? 10000 : Integer.decode(batchsizeStr);
            
            Settings settings = settingsBuilder()
                //.put("http.port", "9200")
                .put("discovery.zen.ping.unicast.hosts", nodeStr)
                .put("discovery.zen.ping.multicast.enabled", false)
                .put("node.master", false)
                .build();
            
            node = nodeBuilder()
                .clusterName(clusterName)
                .client(true)
                .settings(settings)
                .node(); // data(false).

            IndexMigrator im = new IndexMigrator(node.client());
            im.migrateIndex(projectId, shards, batchsize, threads);
        }
        catch (ParseException e) {
            System.err.println("Options parsing failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
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
