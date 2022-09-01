package org.acme.billing;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class InvoiceServiceTest {

    @Inject
    @Channel("prolong")
    Emitter<Invoice> invoiceEmitter;

    @Test
    public void testProlongInvoicePersist() {
        Invoice testInvoice = new Invoice();
        testInvoice.totalPrice = 10;
        testInvoice.type = InvoiceType.PROLONGING;
        testInvoice.numberOfDays = 1;
        testInvoice.rentedCar = "ABC123";

        invoiceEmitter.send(testInvoice);

        given()
            .when().get("/invoice")
            .then()
            .statusCode(200)
            .log();
    }
}
