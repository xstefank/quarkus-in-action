package org.acme.billing.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDate;

public class Invoice extends PanacheMongoEntity {

    public double totalPrice;
    public boolean paid;
    public Reservation reservation;

    public Invoice(double totalPrice, boolean paid, Reservation reservation) {
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "totalPrice=" + totalPrice +
            ", paid=" + paid +
            ", reservation=" + reservation +
            ", id=" + id +
            '}';
    }

    public static final class Reservation {
        public Long id;
        public String userId;
        public Long carId;
        public LocalDate startDay;
        public LocalDate endDay;

        @Override
        public String toString() {
            return "Reservation{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", carId=" + carId +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                '}';
        }
    }
}
