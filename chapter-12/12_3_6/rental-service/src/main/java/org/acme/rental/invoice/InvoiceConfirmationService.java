package org.acme.rental.invoice;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.rental.entity.Rental;
import org.acme.rental.invoice.data.InvoiceConfirmation;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class InvoiceConfirmationService {

    @Incoming("invoices-confirmations")
    public void invoicePaid(InvoiceConfirmation invoiceConfirmation) {
        Log.info("Received invoice confirmation " + invoiceConfirmation);

        if (!invoiceConfirmation.paid) {
            Log.warn("Received unpaid invoice confirmation - "
                + invoiceConfirmation);
            // retry handling omitted
        }

        InvoiceConfirmation.InvoiceReservation reservation =
            invoiceConfirmation.invoice.reservation;

        Rental.findByUserAndReservationIdsOptional(
                reservation.userId, reservation.id)
            .ifPresentOrElse(rental -> {
                // mark the already started rental as paid
                rental.paid = true;
                rental.update();
            }, () -> {
                // create new rental starting in the future
                Rental rental = new Rental();
                rental.userId = reservation.userId;
                rental.reservationId = reservation.id;
                rental.startDate = reservation.startDay;
                rental.active = false;
                rental.paid = true;
                rental.persist();
            });
    }
}
