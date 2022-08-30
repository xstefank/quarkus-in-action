package org.acme.billing.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Invoice extends PanacheEntity {

    public double totalPrice;
    public int numberOfDays;
    public InvoiceType type;

    public String rentedCar;

    public boolean paid;

    @Override
    public String toString() {
        return "Invoice{" +
            "totalPrice=" + totalPrice +
            ", numberOfDays=" + numberOfDays +
            ", type=" + type +
            ", rentedCar='" + rentedCar + '\'' +
            ", paid=" + paid +
            ", id=" + id +
            '}';
    }
}
