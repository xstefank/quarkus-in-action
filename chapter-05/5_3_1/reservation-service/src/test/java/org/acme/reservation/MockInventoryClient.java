package org.acme.reservation;

import io.quarkus.test.Mock;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;

import java.util.List;

@Mock
public class MockInventoryClient implements GraphQLInventoryClient {

    @Override
    public List<Car> allCars() {
        Car peugeot = new Car(1L, "ABC123", "Peugeot", "406");
        return List.of(peugeot);
    }
}
