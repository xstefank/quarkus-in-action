package org.acme.metadata.extension.deployment;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
@ConfigMapping(prefix = "quarkus.metadata")
public interface MetadataExtensionConfig {

    /**
     * The path to the metadata endpoint.
     */
    @WithDefault("/metadata")
    String path();
}
