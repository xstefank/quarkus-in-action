package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class ProducerProcessorConsumer_9_3_2 {

    private final short MAX_INVOCATIONS = 5;
    private short currentInvocation = 0;

    // We need to restrict the number of produced messages because otherwise it would go forever
    @Outgoing("channel-name")
    public String producer() {
        Util.waitFor(1000);
        if (currentInvocation++ < MAX_INVOCATIONS)
            return "hello";
        else
            throw new RuntimeException("Reached maximum invocations. Restart the application to start again.");
    }

    @Incoming("channel-name")
    @Outgoing("channel-name-2")
    public String processor(String payload) {
        return payload.toUpperCase();
    }

    @Incoming("channel-name-2")
    public void consumer(String payload) {
        System.out.println(payload);
    }
}
