package org.acme.rental.reservation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/admin/reservation")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface ReservationClient {

    @GET
    @Path("/{id}")
    Reservation getById(Long id);
}
