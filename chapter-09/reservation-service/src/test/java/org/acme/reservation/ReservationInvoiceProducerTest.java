package org.acme.reservation;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import org.acme.reservation.billing.Invoice;
import org.acme.reservation.entity.Reservation;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.acme.reservation.rest.ReservationResource.STANDARD_RATE_PER_DAY;

@QuarkusTest
@ApplicationScoped
public class ReservationInvoiceProducerTest {

    private final Map<Integer, Invoice> receivedInvoices = new HashMap<>();
    private final AtomicInteger ids = new AtomicInteger(0);

    @Incoming("invoices-amqp")
    public void processInvoice(JsonObject json) {
        Invoice invoice = json.mapTo(Invoice.class);
        System.out.println("Received invoice " + invoice);

        receivedInvoices.put(ids.incrementAndGet(), invoice);
    }

    @AfterEach
    @RunOnVertxContext
    public void cleanup(UniAsserter uniAsserter) {
        final UniAsserter asserter = new UniAsserterInterceptor(uniAsserter) {
            @Override
            protected <T> Supplier<Uni<T>> transformUni(Supplier<Uni<T>> uniSupplier) {
                return () -> Panache.withTransaction(uniSupplier);
            }
        };
        asserter.execute(() -> Reservation.deleteAll());
    }

    @Test
    public void testInvoiceProduced() throws Throwable {
        // Make reservation request that send the invoice to AMQP
        Reservation reservation = new Reservation();
        reservation.carId = 1L;
        reservation.startDay = LocalDate.now().plusDays(1);
        reservation.endDay = reservation.startDay;

        given().body(reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .when().post("/reservation")
            .then().statusCode(200);

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .until(() -> receivedInvoices.size() == 1);

        // Assert that the invoice message was received in this consumer
        Assertions.assertEquals(1, receivedInvoices.size());
        Assertions.assertEquals(STANDARD_RATE_PER_DAY, receivedInvoices.get(1).price);
    }
}
