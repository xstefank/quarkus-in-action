package org.acme.rental;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class RentalEndTest {

    @InjectKafkaCompanion
    KafkaCompanion kafkaCompanion;

    @Test
    public void testRentalProlongedInvoiceSend() throws JsonProcessingException {
        // persist new Rental
        given()
            .when().post("/rental/start/1/1/1")
            .then()
            .statusCode(200);

        // test that the Kafka message is sent to the prolong Kafka topic
        given()
            .when().put("/rental/end/1/1")
            .then()
            .statusCode(200)
            .body("active", is(false),
                "endDate", is(LocalDate.now().toString()));

        // verify that the invoice was sent
        ConsumerTask<String, String> prolongInvoices = kafkaCompanion
            .consumeStrings().fromTopics("prolong", 1).awaitNextRecord(Duration.ofSeconds(2));
        assertEquals(1, prolongInvoices.count());
        assertTrue(prolongInvoices.getFirstRecord().value().contains("\"price\":25.99"));
    }
}
