package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliTest {
    private static final Logger log = LoggerFactory.getLogger(CliTest.class);

    private static final String O_PRJ       = "prj";       //$NON-NLS-1$
    private static final String O_CLUSTER   = "cluster";   //$NON-NLS-1$
    private static final String O_NODE      = "node";      //$NON-NLS-1$
    private static final String O_SHARDS    = "shards";    //$NON-NLS-1$
    private static final String O_THREADS   = "threads";   //$NON-NLS-1$
    private static final String O_BATCHSIZE = "batchsize"; //$NON-NLS-1$

    private void assertOptionValues(CommandLineParser parser, Options opts, String [] args) throws Exception {
        CommandLine line = parser.parse(opts, args);

        String projectIdStr = line.getOptionValue(O_PRJ);
        assertTrue(line.hasOption(O_PRJ));
        assertNotNull(projectIdStr);
        assertEquals("1404", projectIdStr);

        String clusterName = line.getOptionValue(O_CLUSTER);
        assertTrue(line.hasOption(O_CLUSTER));
        assertNotNull(clusterName);
        assertEquals("elasticsearch", clusterName);

        assertFalse(projectIdStr == null || clusterName == null);
    }
    
    @Ignore
    public void testCli() throws Exception {
        Options opts = new Options();
        opts.addOption("p", O_PRJ      , true, "Project id"); //$NON-NLS-1$
        opts.addOption("c", O_CLUSTER  , true, "Cluster name"); //$NON-NLS-1$
        opts.addOption("n", O_NODE     , true, "IP/host the node to connect. Optional, default is localhost"); //$NON-NLS-1$
        //opts.addOption("m", false, "Copy mapping.");
        opts.addOption("s", O_SHARDS   , true, "Number of shards in new index. Optional, default 7"); //$NON-NLS-1$
        opts.addOption("t", O_THREADS  , true, "Number of writer threads. Optional, default 4");  //$NON-NLS-1$
        opts.addOption("b", O_BATCHSIZE, true, "Number of ES documents in the batch for indexing. Optional, default 10000"); //$NON-NLS-1$

        CommandLineParser parser = new GnuParser();

        assertOptionValues(parser, opts, new String [] { "-p", "1404", "-c", "elasticsearch" });                
        assertOptionValues(parser, opts, new String [] { "--prj", "1404", "-cluster", "elasticsearch" });                
        assertOptionValues(parser, opts, new String [] { "--prj=1404", "--cluster=elasticsearch" });                
    }
}
