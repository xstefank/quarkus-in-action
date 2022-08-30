package org.acme.rental.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Rental extends PanacheMongoEntity {

    public Long userId;
    public Long reservationId;
    public LocalDate startDate;
    public LocalDate endDate;
    public boolean active;

    public static Optional<Rental> findByUserAndReservationIdsOptional(Long userId, Long reservationId) {
        return find("userId = ?1 and reservationId = ?2", userId, reservationId).firstResultOptional();
    }

    public static List<Rental> listActive() {
        return list("active", true);
    }
}
