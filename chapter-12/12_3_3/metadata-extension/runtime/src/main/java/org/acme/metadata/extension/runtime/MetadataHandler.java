package org.acme.metadata.extension.runtime;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class MetadataHandler
    implements Handler<RoutingContext> {

    private final String data;

    public MetadataHandler(String data) {
        this.data = data;
    }

    @Override
    public void handle(RoutingContext ctx) {
        ctx.response().putHeader("Content-Type",
            "application/json");
        ctx.end(data);
    }
}
