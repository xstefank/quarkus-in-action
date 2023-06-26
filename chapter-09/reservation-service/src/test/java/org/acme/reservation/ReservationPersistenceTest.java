package org.acme.reservation;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;
import org.acme.reservation.entity.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

@QuarkusTest
public class ReservationPersistenceTest {

    @Test
    @RunOnVertxContext
    public void testCreateReservation(UniAsserter asserter) {
        asserter = new UniAsserterInterceptor(asserter) {
            @Override
            protected <T> Supplier<Uni<T>> transformUni(Supplier<Uni<T>> uniSupplier) {
                return () -> Panache.withTransaction(uniSupplier);
            }
        };

        asserter.assertThat(() -> {
            Reservation reservation = new Reservation();
            reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS);
            reservation.endDay = LocalDate.now().plus(12, ChronoUnit.DAYS);
            reservation.carId = 384L;
            return reservation.<Reservation>persist();
        }, reservation -> Assertions.assertNotNull(reservation.id));

        asserter.assertEquals(() -> Reservation.count(), 1L);
        asserter.assertThat(() -> Reservation.<Reservation>findById(1L),
            reservation -> {
                Assertions.assertNotNull(reservation);
                Assertions.assertEquals(384L, reservation.carId);
            });
    }
}
