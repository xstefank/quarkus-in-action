package org.acme.billing;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.billing.model.Invoice;

import java.util.List;

@Path("/invoice")
public class InvoiceResource {

    @GET
    public Uni<List<Invoice>> getAll() {
        return Invoice.listAll();
    }
}
