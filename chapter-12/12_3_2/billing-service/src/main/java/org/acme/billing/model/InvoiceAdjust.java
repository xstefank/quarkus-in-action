package org.acme.billing.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDate;

public class InvoiceAdjust extends PanacheMongoEntity {

    public String renalId;
    public String userId;
    public LocalDate actualEndDate;
    public double price;
    public boolean paid;

    @Override
    public String toString() {
        return "InvoiceAdjust{" +
            "renalId='" + renalId + '\'' +
            ", userId='" + userId + '\'' +
            ", actualEndDate=" + actualEndDate +
            ", price=" + price +
            ", paid=" + paid +
            ", id=" + id +
            '}';
    }
}
