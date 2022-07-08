package org.acme.reservation.reservation;

import java.time.LocalDate;

public class Reservation {

    public Long id;
    public Long carId;
    public LocalDate startDay;
    public LocalDate endDay;

    public boolean isReserved(LocalDate startDay, LocalDate endDay) {
        return (!(this.endDay.isBefore(startDay) || this.startDay.isAfter(endDay)));
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", carId=" + carId +
            ", startDay=" + startDay +
            ", endDay=" + endDay +
            '}';
    }
}
