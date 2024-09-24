package org.acme.reservation.reservation;

import java.time.LocalDate;

public class Reservation {

    public Long id;
    public Long carId;
    public String userId;
    public LocalDate startDay;
    public LocalDate endDay;

    /**
     * Check if the given duration overlaps with this reservation
     * @return true if the dates overlap with the reservation, false
     * otherwise
     */
    public boolean isReserved(LocalDate startDay, LocalDate endDay) {
        return (!(this.endDay.isBefore(startDay) ||
            this.startDay.isAfter(endDay)));
    }
}
