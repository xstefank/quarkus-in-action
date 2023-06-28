package org.acme.rental.invoice.data;

public class InvoiceConfirmation {

    public Invoice invoice;
    public boolean paid;

    @Override
    public String toString() {
        return "InvoiceConfirmation{invoice=%s, paid=%s}".formatted(invoice, paid);
    }
}
