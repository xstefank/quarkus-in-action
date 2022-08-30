package org.acme.billing;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceType;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/invoice")
@ApplicationScoped
public class InvoiceResource {

    @GET
    public Uni<List<PanacheEntityBase>> getAllInvoices() {
        return Invoice.listAll();
    }

    @GET
    @Path("/persist")
    @ReactiveTransactional
    public Uni<PanacheEntityBase> persist() {
        Invoice invoice = new Invoice();
        invoice.totalPrice = 19.99;
        invoice.numberOfDays = 1;
        invoice.type = InvoiceType.RESERVATION;
        invoice.rentedCar = "asdf asdf";

        return invoice.persist().onItem().invoke(result -> System.out.println(result));
    }


}
