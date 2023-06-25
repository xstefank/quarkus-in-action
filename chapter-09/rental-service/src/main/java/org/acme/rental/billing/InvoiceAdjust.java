package org.acme.rental.billing;

import org.acme.rental.entity.Rental;

import java.time.LocalDate;

public class InvoiceAdjust {

    public Rental rental;
    public LocalDate actualEndDate;
    public double price;


    public InvoiceAdjust(Rental rental, LocalDate actualEndDate, double price) {
        this.rental = rental;
        this.actualEndDate = actualEndDate;
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvoiceAdjust{" +
            "rental=" + rental +
            ", actualEndDate=" + actualEndDate +
            ", price=" + price +
            '}';
    }
}
