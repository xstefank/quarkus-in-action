package org.acme.statistics;

import io.quarkus.funqy.Funq;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.acme.statistics.inventory.GraphQLInventoryClient;

import java.time.Instant;

public class CarStatistics {

    @Inject
    @GraphQLClient("inventory")
    GraphQLInventoryClient inventoryClient;

    @Funq
    public Uni<String> getCarStatistics() {
        return inventoryClient.allCars()
            .map(cars -> ("The Car Rental car statistics created at %s. " +
                "Number of available cars: %d")
                .formatted(Instant.now(), cars.size()));
    }
}
