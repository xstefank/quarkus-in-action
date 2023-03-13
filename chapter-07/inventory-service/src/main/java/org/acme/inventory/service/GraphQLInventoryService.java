package org.acme.inventory.service;

import org.acme.inventory.model.Car;
import org.acme.inventory.repository.CarRepository;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@GraphQLApi
public class GraphQLInventoryService {

    @Inject
    CarRepository carRepository;

    @Query
    public List<Car> cars() {
        return carRepository.listAll();
    }

    @Transactional
    @Mutation
    public Car register(Car car) {
        carRepository.persist(car);
        return car;
    }

    @Mutation
    public boolean remove(String licensePlateNumber) {
        Optional<Car> toBeRemoved = carRepository
            .findByLicensePlateNumberOptional(
                licensePlateNumber);
        if (toBeRemoved.isPresent()) {
            carRepository.delete(toBeRemoved.get());
            return true;
        } else {
            return false;
        }
    }
}
