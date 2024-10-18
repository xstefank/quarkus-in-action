package org.acme.metadata.extension.deployment;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.bootstrap.model.ApplicationModel;
import io.quarkus.deployment.BootstrapConfig;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.AppModelProviderBuildItem;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.vertx.http.deployment.RouteBuildItem;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.acme.metadata.extension.runtime.MetadataExtensionRecorder;

class MetadataExtensionProcessor {

    private static final String FEATURE = "metadata-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    RouteBuildItem createRoute(MetadataExtensionRecorder recorder,
                               ApplicationInfoBuildItem info,
                               AppModelProviderBuildItem appModel,
                               MetadataExtensionConfig config) {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("name", info.getName());
            json.put("version", info.getVersion());
        ArrayNode dependenciesArray = json.putArray("dependencies");
        ApplicationModel applicationModel = appModel
            .validateAndGet(new BootstrapConfig());
        applicationModel.getRuntimeDependencies().forEach(dep -> {
            dependenciesArray.addObject()
                .put("artifact-id", dep.getArtifactId())
                .put("group-id", dep.getGroupId());
        });
        Handler<RoutingContext> handler = recorder
            .createHandler(json.toString());
        return new RouteBuildItem.Builder()
            .handler(handler)
            .route(config.path())
            .build();
    }
}
