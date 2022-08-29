package org.acme.reservation.billing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class BillingService {

    @Incoming("invoices")
    public CompletionStage<Void> processInvoice(Message<Invoice> invoice) throws Exception {
        System.out.println(invoice.getPayload());
        return invoice.nack(new Exception("not paid"));
//        return invoice.ack();
    }
}
