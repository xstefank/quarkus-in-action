package org.acme.billing;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceType;
import org.eclipse.microprofile.reactive.messaging.Channel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.function.Consumer;

@Path("/invoice")
@ApplicationScoped
public class InvoiceResource {

    @GET
    public Uni<List<PanacheEntityBase>> getAllInvoices() {
        return Invoice.listAll();
    }

//    @Inject
//    @Channel("prolong")
//    MutinyEmitter<Invoice> prolongEmitter;
//
//    @GET
//    @Path("/persist")
//    //    @ReactiveTransactional
//    public Uni<Void> persist() {
//        Invoice invoice = new Invoice();
//        invoice.totalPrice = 19.99;
//        invoice.numberOfDays = 1;
//        invoice.type = InvoiceType.RESERVATION;
//        invoice.rentedCar = "asdf asdf";
//
//        return prolongEmitter.send(invoice);
//
////        return invoice.persist().onItem().invoke(result -> System.out.println(result));
//    }


}
