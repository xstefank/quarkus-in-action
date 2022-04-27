package org.acme.reservation.rental;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "rental")
public interface RentalClient {
}
