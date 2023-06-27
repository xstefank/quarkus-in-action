package org.acme.billing;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.billing.data.Reservation;
import org.acme.billing.data.ReservationInvoiceMessage;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceType;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.Map;

@ApplicationScoped
public class InvoiceProcessor {

    @Incoming("invoices")
    @Outgoing("invoices-requests")
    @WithTransaction
    public Uni<Invoice> processInvoice(Message<JsonObject> message) {
        ReservationInvoiceMessage invoiceMessage = message.getPayload().mapTo(ReservationInvoiceMessage.class);
        Reservation reservation = invoiceMessage.reservation;
        Invoice invoice = new Invoice(invoiceMessage.price, InvoiceType.RESERVATION,
            false, createDetails(reservation));

        return invoice.<Invoice>persist()
            .onItem().invoke(message::ack);
    }

    private Map<String, String> createDetails(Reservation reservation) {
        return Map.of(
            "Reservation ID", reservation.id.toString(),
            "User", reservation.userId,
            "Car ID", reservation.carId.toString(),
            "Start date", reservation.startDay.toString(),
            "End date", reservation.endDay.toString()
        );
    }
}
