package org.acme;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import java.time.Duration;

@Path("/ticks")
public class TicksResource {

    @Inject
    @Channel("ticks")
    Publisher<Long> ticks;

    @GET
    @Path("/consume")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<Long> sseTicks() {
        return ticks;
    }


    // Copied from 9-3-2-reactive-messaging-ticks without limit

    @Outgoing("ticks")
    public Multi<Long> aFewTicks() {
        return Multi.createFrom()
            .ticks().every(Duration.ofSeconds(1));
    }
}
