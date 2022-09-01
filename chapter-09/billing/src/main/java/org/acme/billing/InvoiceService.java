package org.acme.billing;

import io.smallrye.mutiny.Uni;
import org.acme.billing.model.Invoice;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class InvoiceService {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Incoming("prolong")
    public Uni<Void> consume(Invoice invoice) {
        return sessionFactory.openSession()
            .chain(s -> s.withTransaction(t -> s.persist(invoice))
                .onTermination().call(s::close));
    }
}
