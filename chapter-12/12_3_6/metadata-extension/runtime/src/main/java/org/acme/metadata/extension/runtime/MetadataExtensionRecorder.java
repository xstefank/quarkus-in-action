package org.acme.metadata.extension.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@Recorder
public class MetadataExtensionRecorder {
    public Handler<RoutingContext> createHandler(String data) {
        return new MetadataHandler(data);
    }
}
