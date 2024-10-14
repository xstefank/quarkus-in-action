package org.acme.billing;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.billing.data.InvoiceConfirmation;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceAdjust;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
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
        payment(invoice.reservation.userId, invoice.totalPrice, invoice);

        invoice.paid = true;
        invoice.update();
        Log.infof("Invoice %s is paid.", invoice);

        return new InvoiceConfirmation(invoice, true);
    }

    @Incoming("invoices-adjust")
    @Blocking
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public void requestAdjustment(InvoiceAdjust invoiceAdjust) {
        Log.info("Received invoice adjustment: " + invoiceAdjust);

        payment(invoiceAdjust.userId, invoiceAdjust.price, invoiceAdjust);
        invoiceAdjust.paid = true;
        invoiceAdjust.persist();
        Log.infof("Invoice adjustment %s is paid.", invoiceAdjust);
    }

    private void payment(String user, double price, Object data) {
        Log.infof("Request for payment user: %s, price: %f, data: %s",
            user, price, data);
        try {
            Thread.sleep(random.nextInt(1000, 5000));
        } catch (InterruptedException e) {
            Log.error("Sleep interrupted.", e);
        }
    }
}
