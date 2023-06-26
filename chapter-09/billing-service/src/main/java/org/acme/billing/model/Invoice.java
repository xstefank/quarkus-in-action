package org.acme.billing.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import java.util.Map;

@Entity
public class Invoice extends PanacheEntity {

    public double totalPrice;
    public InvoiceType type;
    public boolean paid;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    public Map<String, String> details;

    public Invoice() {}
    
    public Invoice(double totalPrice, InvoiceType type, boolean paid, Map<String, String> details) {
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
