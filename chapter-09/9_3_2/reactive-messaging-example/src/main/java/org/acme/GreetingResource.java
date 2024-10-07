package org.acme;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;
import java.time.Instant;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @Outgoing("ticks")
    public Multi<Long> aFewTicks() {
        return Multi.createFrom()
            .ticks().every(Duration.ofSeconds(1))
            .select().first(5);
    }

    @Incoming("ticks")
    @Outgoing("times")
    public Multi<String> processor(Multi<Long> ticks) {
        return ticks.map(tick -> Instant.now().toString());
    }

    @Incoming("times")
    public void consumer(String payload) {
        System.out.println(payload);
    }

}
