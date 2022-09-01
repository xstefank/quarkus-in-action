package org.acme.rental.reservation;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(baseUri = "http://localhost:8081")
@Path("/admin")
public interface ReservationClient {

    @GET
    @Path("/reservation/{reservationId}")
    Reservation getReservationById(@PathParam("reservationId") Long reservationId);
}

