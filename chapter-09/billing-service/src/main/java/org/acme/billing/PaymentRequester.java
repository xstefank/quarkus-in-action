package org.acme.billing;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.billing.data.InvoiceConfirmation;
import org.acme.billing.model.Invoice;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.Random;

@ApplicationScoped
public class PaymentRequester {

    private final Random random = new Random();

    @Incoming("invoices-requests")
    @Outgoing("invoices-confirmations")
    @Blocking
    public InvoiceConfirmation requestPayment(Invoice invoice) {
        try {
            Thread.sleep(random.nextInt(1000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        invoice.paid = true;

        return new InvoiceConfirmation(invoice, true);
    }

    @Incoming("invoices-confirmations")
    public void consume(InvoiceConfirmation invoiceConfirmation) {
        System.out.println(invoiceConfirmation);
    }
}
