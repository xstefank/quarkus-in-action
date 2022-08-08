package org.acme.reservation.mock;

import io.quarkus.test.Mock;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@Mock
@ApplicationScoped
public class MockInventoryClient implements GraphQLInventoryClient {

    @Override
    public List<Car> allCars() {
        List<Car> cars = new ArrayList<>();
        Car peugeot = new Car(1L, "ABC 123", "Peugeot", "406");
        cars.add(peugeot);
        return cars;
    }

}
