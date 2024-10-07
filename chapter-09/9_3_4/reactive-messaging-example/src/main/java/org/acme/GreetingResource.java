package org.acme;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;

@Path("/ticks")
public class GreetingResource {

    @Inject
    @Channel("ticks")
    Multi<Long> ticks;

    @GET
    @Path("/consume")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Long> sseTicks() {
        return ticks;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @Outgoing("ticks")
    public Multi<Long> aFewTicks() {
        return Multi.createFrom()
            .ticks().every(Duration.ofSeconds(1)); // we also removed the select for the first 5 items
    }
}
