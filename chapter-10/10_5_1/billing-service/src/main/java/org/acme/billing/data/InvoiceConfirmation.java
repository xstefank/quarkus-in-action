package org.acme.billing.data;

import org.acme.billing.model.Invoice;

public class InvoiceConfirmation {

    public Invoice invoice;
    public boolean paid;

    public InvoiceConfirmation(Invoice invoice, boolean paid) {
        this.invoice = invoice;
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "InvoiceConfirmation{" +
            "invoice=" + invoice +
            ", paid=" + paid +
            '}';
    }
}
