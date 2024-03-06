package org.acme.billing.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDate;

public class Invoice extends PanacheMongoEntity {

    public double totalPrice;
    public boolean paid;
    public Reservation reservation;

    public Invoice() {
    }

    public Invoice(double totalPrice, boolean paid, Reservation reservation) {
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return ("Invoice{totalPrice=%s, paid=%s," +
            " reservation=%s, id=%s}")
            .formatted(totalPrice, paid, reservation, id);
    }

    public static final class Reservation {
        public Long id;
        public String userId;
        public Long carId;
        public LocalDate startDay;
        public LocalDate endDay;

        @Override
        public String toString() {
            return "Reservation{id=%d, userId='%s', carId=%d, startDay=%s, endDay=%s}"
                .formatted(id, userId, carId, startDay, endDay);
        }
    }
}
