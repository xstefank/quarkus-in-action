package org.acme.inventory.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.inventory.model.Car;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    public Optional<Car> findByLicensePlateNumberOptional(
        String licensePlateNumber) {
        return find("licensePlateNumber", licensePlateNumber)
            .firstResultOptional();
    }
}
