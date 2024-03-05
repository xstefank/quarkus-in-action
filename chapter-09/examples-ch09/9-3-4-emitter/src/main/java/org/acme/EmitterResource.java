package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Path("/emitter")
public class EmitterResource {

    @Inject
    @Channel("requests")
    Emitter<String> requestsEmitter;

    @POST
    @Path("/request")
    public String request(String body) {
        requestsEmitter.send(body);
        return "Processing " + body;
    }

    @Incoming("requests")
    public void consumer(String payload) {
        System.out.println("Received payload: " + payload);
    }
}
