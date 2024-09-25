package org.acme.rental.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Rental extends PanacheMongoEntity {

    public String userId;
    public Long reservationId;
    public LocalDate startDate;
    public LocalDate endDate;
    public boolean active;

    public static Optional<Rental> findByUserAndReservationIdsOptional(
        String userId, Long reservationId) {
        return find("userId = ?1 and reservationId = ?2",
            userId, reservationId)
            .firstResultOptional();
    }

    public static List<Rental> listActive() {
        return list("active", true);
    }

    @Override
    public String toString() {
        return "Rental{" +
            "userId='" + userId + '\'' +
            ", reservationId=" + reservationId +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", active=" + active +
            ", id=" + id +
            '}';
    }
}
