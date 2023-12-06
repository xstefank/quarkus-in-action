package org.acme.rental.reservation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/admin/reservation")
@RegisterRestClient(configKey = "reservation")
public interface ReservationClient {

    @GET
    @Path("/{id}")
    Reservation getById(Long id);
}
