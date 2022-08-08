package org.acme.reservation.rental;

import java.time.LocalDate;

public class Rental {

    private final Long id;
    private final Long userId;
    private final Long reservationId;
    private final LocalDate startDate;

    public Rental(Long id, Long userId, Long reservationId,
                  LocalDate startDate) {
        this.id = id;
        this.userId = userId;
        this.reservationId = reservationId;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
