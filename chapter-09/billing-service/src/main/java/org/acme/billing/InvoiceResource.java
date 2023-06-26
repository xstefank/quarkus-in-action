package org.acme.billing;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceType;

import java.util.List;
import java.util.Map;

@Path("/invoice")
public class InvoiceResource {

    @GET
    public Uni<List<Invoice>> getAll() {
        return Invoice.listAll();
    }

    @GET
    @Path("/create")
    @WithTransaction
    public Uni<Invoice> createTest() {
        Invoice invoice = new Invoice(20, InvoiceType.REFUND, true, Map.of("test", "value", "test2", "value2"));
        return invoice.persist();
    }
}
