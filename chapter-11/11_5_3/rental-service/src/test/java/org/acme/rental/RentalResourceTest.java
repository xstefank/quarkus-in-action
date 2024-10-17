package org.acme.rental;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import org.acme.rental.reservation.Reservation;
import org.acme.rental.reservation.ReservationClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class RentalResourceTest {

    @InjectKafkaCompanion
    KafkaCompanion kafkaCompanion;

    @Test
    public void testRentalProlongedInvoiceSend() {
        // stub the ReservationClient call
        Reservation reservation = new Reservation();
        reservation.endDay = LocalDate.now().minusDays(1);

        ReservationClient mock = Mockito.mock(ReservationClient.class);
        Mockito.when(mock.getById(1L)).thenReturn(reservation);
        QuarkusMock.installMockForType(mock, ReservationClient.class,
            RestClient.LITERAL);

        // start new rental for reservation with id 1
        given()
            .when().post("/rental/start/user123/1")
            .then().statusCode(200);

        // end the rental with one prolonged day
        given()
            .when().put("/rental/end/user123/1")
            .then().statusCode(200)
            .body("active", is(false),
                "endDate", is(LocalDate.now().toString()));

        // verify that message is sent to the invoices-adjust Kafka topic
        ConsumerTask<String, String> invoiceAdjust = kafkaCompanion
            .consumeStrings().fromTopics("invoices-adjust", 1)
            .awaitNextRecord(Duration.ofSeconds(10));

            assertEquals(1, invoiceAdjust.count());
        assertTrue(invoiceAdjust.getFirstRecord().value()
            .contains("\"price\":" +
                RentalResource.STANDARD_PRICE_FOR_PROLONGED_DAY));
    }
}

