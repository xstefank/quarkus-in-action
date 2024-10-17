package org.acme;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.openshift.client.OpenShiftClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/pods")
public class PodResource {

    @Inject
    OpenShiftClient openShiftClient;

    @GET
    public List<Pod> pods() {
        return openShiftClient.pods().inNamespace("mstefank-dev")
            .withField("status.phase", "Running").list().getItems();
    }
}
