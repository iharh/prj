package com.clarabridge.elasticsearch.index.migrate.plugin;

import org.elasticsearch.common.inject.Inject;

import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.BytesRestResponse;

import org.elasticsearch.indices.IndicesService;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

public class MigrateRestHandler implements RestHandler {
    private IndicesService indicesService;

    @Inject
    public MigrateRestHandler(RestController restController, IndicesService indicesService) {
        restController.registerHandler(GET, "/_hello", this); // $NON-NLS-1$
        this.indicesService = indicesService;
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        String who = request.param("who"); // $NON-NLS-1$
        //String whoSafe = (who != null) ? who : "world"; // $NON-NLS-1$
        String whoSafe = (indicesService != null) ? "indicesService" : "null"; // $NON-NLS-1$ $NON-NLS-2$

        channel.sendResponse(new BytesRestResponse(OK, "Hello, " + whoSafe)); // $NON-NLS-1$
    }
}
