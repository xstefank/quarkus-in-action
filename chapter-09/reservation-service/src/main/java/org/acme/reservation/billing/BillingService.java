package org.acme.reservation.billing;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BillingService {

    @Incoming("invoices")
    public void processInvoice(Invoice invoice) {
        System.out.println("Processing received invoice: " + invoice);
    }
}
