package org.acme.rental.billing;

import org.acme.rental.entity.Rental;

import java.time.LocalDate;

public class Invoice {

    public Long userId;
    public Rental rental;
    public LocalDate originalEndDate;
    public double price;

    public Invoice(Long userId, Rental rental, LocalDate originalEndDate, double price) {
        this.userId = userId;
        this.rental = rental;
        this.originalEndDate = originalEndDate;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "userId=" + userId +
            ", rental=" + rental +
            ", originalEndDate=" + originalEndDate +
            ", price=" + price +
            '}';
    }
}
