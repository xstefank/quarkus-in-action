package org.acme.rental.reservation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "reservation")
public interface ReservationClient {

    @GET
    @Path("/admin/reservation/{id}")
    Reservation getById(Long id);
}
