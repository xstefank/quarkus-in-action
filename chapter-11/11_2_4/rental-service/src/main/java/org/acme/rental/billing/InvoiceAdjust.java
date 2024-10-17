package org.acme.rental.billing;

import java.time.LocalDate;

public class InvoiceAdjust {

    public String rentalId;
    public String userId;
    public LocalDate actualEndDate;
    public double price;

    public InvoiceAdjust(String rentalId, String userId,
                         LocalDate actualEndDate, double price) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.actualEndDate = actualEndDate;
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvoiceAdjust{" +
            "rentalId='" + rentalId + '\'' +
            ", userId='" + userId + '\'' +
            ", actualEndDate=" + actualEndDate +
            ", price=" + price +
            '}';
    }
}
