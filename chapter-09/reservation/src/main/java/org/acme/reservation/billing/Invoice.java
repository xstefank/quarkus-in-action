package org.acme.reservation.billing;

import org.acme.reservation.entity.Reservation;

public class Invoice {

    public Long userId;
    public Reservation reservation;
    public double price;

    public Invoice(Long userId, Reservation reservation, double price) {
        this.userId = userId;
        this.reservation = reservation;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "userId=" + userId +
            ", reservation=" + reservation +
            ", price=" + price +
            '}';
    }
}
