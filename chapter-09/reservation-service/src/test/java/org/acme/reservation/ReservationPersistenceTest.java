package org.acme.reservation;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.reservation.entity.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@QuarkusTest
public class ReservationPersistenceTest {

    @Test
    @Transactional
    public void testCreateReservation() {
        Reservation reservation = new Reservation();
        reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS);
        reservation.endDay = LocalDate.now().plus(12, ChronoUnit.DAYS);
        reservation.carId = 384L;
        reservation.persist().await().indefinitely();

        Assertions.assertNotNull(reservation.id);
        Assertions.assertEquals(1, Reservation.count().await().indefinitely());
        Reservation persistedReservation = Reservation.<Reservation>findById(reservation.id).await().indefinitely();
        Assertions.assertNotNull(persistedReservation);
        Assertions.assertEquals(reservation.carId, persistedReservation.carId);
    }
}
