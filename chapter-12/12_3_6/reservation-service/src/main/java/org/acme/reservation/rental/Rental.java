package org.acme.reservation.rental;

import java.time.LocalDate;

public class Rental {

    private final String id;
    private final String userId;
    private final Long reservationId;
    private final LocalDate startDate;

    public Rental(String id, String userId, Long reservationId,
                  LocalDate startDate) {
        this.id = id;
        this.userId = userId;
        this.reservationId = reservationId;
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "Rental{" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", reservationId=" + reservationId +
            ", startDate=" + startDate +
            '}';
    }
}
