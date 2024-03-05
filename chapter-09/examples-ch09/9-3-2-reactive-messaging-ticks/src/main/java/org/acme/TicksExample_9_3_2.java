package org.acme;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;
import java.time.Instant;

public class TicksExample_9_3_2 {

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
