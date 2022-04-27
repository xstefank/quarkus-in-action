package org.acme.inventory.database;

import org.acme.inventory.model.Car;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class CarInventory {

    private List<Car> cars;

    @PostConstruct
    void initialize() {
        cars = new CopyOnWriteArrayList<>();
        initialData();
    }

    public List<Car> getCars() {
        return cars;
    }

    private void initialData() {
        Car mazda = new Car();
        mazda.manufacturer = "Mazda";
        mazda.model = "6";
        mazda.licensePlateNumber = "ABC123";
        cars.add(mazda);

        Car ford = new Car();
        ford.manufacturer = "Ford";
        ford.model = "Mustang";
        ford.licensePlateNumber = "XYZ987";
        cars.add(ford);
    }

}
