package org.acme;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class AcknowledgmentExample_9_3_3 {

    @Inject
    @Channel("channel-name")
    MutinyEmitter<String> channelEmitter;

    public void producedMessages(@Observes StartupEvent event) {
        sendMessage("OK");
        sendMessage("OK");
        sendMessage("failure");
    }

    private void sendMessage(String text) {
        channelEmitter.send(text)
            .onItem().invoke(() -> System.out.printf("Message %s acked.%n", text))
            .onFailure().invoke(() -> System.out.printf("Message %s nacked.%n", text))
            .subscribe().with(unused -> {});
    }

    @Incoming("channel-name")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<Void> consumer(Message<String> message) {
        if (processMessage(message.getPayload())) {
            return message.ack();
        } else {
            return message.nack(
                new IllegalStateException(
                    "Cannot process message " + message.getPayload()));
        }
    }

    private boolean processMessage(String payload) {
        return !payload.equals("failure");
    }

}
