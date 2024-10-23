package org.acme.billing.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDate;

public class InvoiceAdjust extends PanacheMongoEntity {

    public String rentalId;
    public String userId;
    public LocalDate actualEndDate;
    public double price;
    public boolean paid;

    @Override
    public String toString() {
        return "InvoiceAdjust{" +
            "rentalId='" + rentalId + '\'' +
            ", userId='" + userId + '\'' +
            ", actualEndDate=" + actualEndDate +
            ", price=" + price +
            ", paid=" + paid +
            ", id=" + id +
            '}';
    }
}
