package org.acme.reservation.rental;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

@RegisterRestClient(baseUri = "http://localhost:8082")
@Path("/rental")
public interface RentalClient {

    @POST
    @Path("/start/{userId}/{reservationId}/{carId}")
    Uni<Rental> start(@RestPath Long userId, @RestPath Long reservationId, @RestPath Long carId);

}
