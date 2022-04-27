package org.acme.reservation.rest;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.acme.reservation.database.Reservations;
import org.acme.reservation.Reservation;

@Path("reservation")
public class ReservationResource {

    private final Reservations reservations;

    public ReservationResource(Reservations reservations) {
        this.reservations = reservations;
    }

    @GET
    public List<Reservation> all() {
        return reservations.all();
    }
}
