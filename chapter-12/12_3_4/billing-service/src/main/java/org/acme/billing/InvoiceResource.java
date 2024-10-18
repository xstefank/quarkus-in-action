package org.acme.billing;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.billing.model.Invoice;

import java.util.List;

@Path("/invoice")
public class InvoiceResource {

    @GET
    public List<Invoice> allInvoices() {
        return Invoice.listAll();
    }
}
