package org.acme.rental;

import io.quarkus.test.Mock;
import org.acme.rental.reservation.Reservation;
import org.acme.rental.reservation.ReservationClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@Mock
@RestClient
public class ReservationClientStub implements ReservationClient {

    @Override
    public Reservation getReservationById(Long reservationId) {
        Reservation reservation = new Reservation();
        reservation.endDay = LocalDate.now().minusDays(1);
        return reservation;
    }
}
