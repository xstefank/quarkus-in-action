package org.acme.billing.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.acme.billing.data.Reservation;

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
}
