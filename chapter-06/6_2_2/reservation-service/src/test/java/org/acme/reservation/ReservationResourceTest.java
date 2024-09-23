package org.acme.reservation;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.DisabledOnIntegrationTest;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;
import org.acme.reservation.reservation.Reservation;
import org.acme.reservation.rest.ReservationResource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ReservationResourceTest {

    @TestHTTPEndpoint(ReservationResource.class)
    @TestHTTPResource
    URL reservationResource;

    @TestHTTPEndpoint(ReservationResource.class)
    @TestHTTPResource("availability")
    URL availability;

    @Test
    public void testReservationIds() {
        Reservation reservation = new Reservation();
            reservation.carId = 12345L;
        reservation.startDay = LocalDate
            .parse("2025-03-20");
        reservation.endDay = LocalDate
            .parse("2025-03-29");
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(reservation)
            .when()
            .post(reservationResource)
            .then()
            .statusCode(200)
            .body("id", notNullValue());
    }

    @DisabledOnIntegrationTest(forArtifactTypes =
        DisabledOnIntegrationTest.ArtifactType.NATIVE_BINARY)
    @Test
    public void testMakingAReservationAndCheckAvailability() {
        GraphQLInventoryClient mock =
            Mockito.mock(GraphQLInventoryClient.class);
            Car peugeot = new Car(1L, "ABC123", "Peugeot", "406");
        Mockito.when(mock.allCars())
            .thenReturn(Collections.singletonList(peugeot));
        QuarkusMock.installMockForType(mock,
            GraphQLInventoryClient.class);

        String startDate = "2022-01-01";
        String endDate = "2022-01-10";
        // List available cars for our requested timeslot and choose one
        Car[] cars = RestAssured.given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when().get(availability)
            .then().statusCode(200)
            .extract().as(Car[].class);
        Car car = cars[0];

        // Prepare a Reservation object
        Reservation reservation = new Reservation();
        reservation.carId = car.id;
        reservation.startDay = LocalDate.parse(startDate);
        reservation.endDay = LocalDate.parse(endDate);

        // Submit the reservation
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(reservation)
            .when().post(reservationResource)
            .then().statusCode(200)
            .body("carId", is(car.id.intValue()));

        // Verify that this car doesn't show as available anymore
        RestAssured.given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when().get(availability)
            .then().statusCode(200)
            .body("findAll { car -> car.id == " + car.id + "}", hasSize(0));
    }

}
