package org.acme.rental.invoice;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.rental.entity.Rental;
import org.acme.rental.invoice.data.InvoiceConfirmation;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.time.LocalDate;

@ApplicationScoped
public class InvoiceConfirmationService {

    @Incoming("invoices-confirmations")
    @Blocking
    public void invoicePaid(InvoiceConfirmation invoiceConfirmation) {
        Log.info("Received invoice confirmation " + invoiceConfirmation);

        if (!invoiceConfirmation.paid) {
            Log.warn("Received unpaid invoice confirmation - "
                + invoiceConfirmation);
            // retry handling omitted
        }

        // The details contain the Reservation object representation
        JsonObject details = invoiceConfirmation.invoice.details;

        String userId = details.getString("userId");
        Long reservationId = details.getLong("id");

        Rental
            .findByUserAndReservationIdsOptional(userId, reservationId)
            .ifPresentOrElse(rental -> {
                // mark the already started rental as paid
                rental.paid = true;
                rental.update();
            }, () -> {
                // create new rental starting in the future
                Rental rental = new Rental();
                rental.userId = userId;
                rental.reservationId = reservationId;
                rental.startDate = LocalDate.parse(
                    details.getString("startDay"));
                rental.active = false;
                rental.paid = true;
                rental.persist();
            });
    }
}
