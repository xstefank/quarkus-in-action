package org.acme.reservation.rental;

import javax.ws.rs.POST;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

@RegisterRestClient(baseUri = "http:localhost:8084")
public interface RentalClient {

    @POST
    Rental start(@RestPath Long userId, @RestPath Long reservationId);

}
