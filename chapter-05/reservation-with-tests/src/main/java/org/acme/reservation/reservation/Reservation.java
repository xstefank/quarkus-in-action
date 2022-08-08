package org.acme.reservation.reservation;

import java.time.LocalDate;

public class Reservation {

    public Long id;
    public Long carId;
    public LocalDate startDay;
    public LocalDate endDay;

    /**
     * Check and see if the given start and end days overlap
     * with this reservation
     * @return if the dates overlap with the reservation
     */
    public boolean isReserved(LocalDate startDay, LocalDate endDay) {
        return (!(this.endDay.isBefore(startDay) ||
            this.startDay.isAfter(endDay)));
    }
}
