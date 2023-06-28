package org.acme.billing.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.Entity;

@Entity
public class Invoice extends PanacheEntity {

    public double totalPrice;
    public InvoiceType type;
    public boolean paid;

    public JsonObject details;

    public Invoice() {}
    
    public Invoice(double totalPrice, InvoiceType type, boolean paid, JsonObject details) {
        this.totalPrice = totalPrice;
        this.type = type;
        this.paid = paid;
        this.details = details;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "totalPrice=" + totalPrice +
            ", type=" + type +
            ", paid=" + paid +
            ", details=" + details +
            ", id=" + id +
            '}';
    }
}
