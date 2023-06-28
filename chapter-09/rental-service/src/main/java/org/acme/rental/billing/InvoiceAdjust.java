package org.acme.rental.billing;

import java.time.LocalDate;

public class InvoiceAdjust {

    public String rentalId;
    public LocalDate actualEndDate;
    public double price;


    public InvoiceAdjust(String rentalId, LocalDate actualEndDate,
                         double price) {
        this.rentalId = rentalId;
        this.actualEndDate = actualEndDate;
        this.price = price;
    }

    @Override
    public java.lang.String toString() {
        return "InvoiceAdjust{" +
            "rentalId=" + rentalId +
            ", actualEndDate=" + actualEndDate +
            ", price=" + price +
            '}';
    }
}
