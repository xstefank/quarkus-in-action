package org.acme.rental.invoice.data;

import java.time.LocalDate;

public class InvoiceConfirmation {

    public Invoice invoice;
    public boolean paid;

    @Override
    public String toString() {
        return "InvoiceConfirmation{" +
            "invoice=" + invoice +
            ", paid=" + paid +
            '}';
    }

    public static final class Invoice {
        public boolean paid;
        public InvoiceReservation reservation;

        @Override
        public String toString() {
            return "Invoice{" +
                "paid=" + paid +
                ", reservation=" + reservation +
                '}';
        }
    }

    public static final class InvoiceReservation {
        public Long id;
        public String userId;
        public LocalDate startDay;

        @Override
        public String toString() {
            return "InvoiceReservation{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", startDay=" + startDay +
                '}';
        }
    }
}
