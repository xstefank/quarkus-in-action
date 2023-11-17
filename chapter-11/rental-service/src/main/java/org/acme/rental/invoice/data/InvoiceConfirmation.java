package org.acme.rental.invoice.data;

import java.time.LocalDate;

public class InvoiceConfirmation {

    public Invoice invoice;
    public boolean paid;

    @Override
    public String toString() {
        return "InvoiceConfirmation{invoice=%s, paid=%s}"
            .formatted(invoice, paid);
    }

    public static final class Invoice {
        public boolean paid;
        public Reservation reservation;

        @Override
        public String toString() {
            return "Invoice{paid=%s, reservation=%s}"
                .formatted(paid, reservation);
        }
    }

    public static final class Reservation {
        public Long id;
        public String userId;
        public LocalDate startDay;

        @Override
        public String toString() {
            return "Reservation{userId='%s', id=%d, startDay=%s}"
                .formatted(userId, id, startDay);
        }
    }
}
