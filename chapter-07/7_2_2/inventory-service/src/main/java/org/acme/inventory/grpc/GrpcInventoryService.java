package org.acme.inventory.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.inventory.model.Car;
import org.acme.inventory.model.CarResponse;
import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryService;
import org.acme.inventory.model.RemoveCarRequest;
import org.acme.inventory.repository.CarRepository;

import java.util.Optional;

@GrpcService
public class GrpcInventoryService implements InventoryService {

    @Inject
    CarRepository carRepository;

    @Override
    @Blocking
    public Multi<CarResponse> add(Multi<InsertCarRequest> requests) {
        return requests
            .map(request -> {
                Car car = new Car();
                car.setLicensePlateNumber(request.getLicensePlateNumber());
                car.setManufacturer(request.getManufacturer());
                car.setModel(request.getModel());
                return car;
            }).onItem().invoke(car -> {
                QuarkusTransaction.requiringNew().run( () -> {
                    carRepository.persist(car);
                    Log.info("Persisting " + car);
                });
            }).map(car -> CarResponse.newBuilder()
                .setLicensePlateNumber(car.getLicensePlateNumber())
                .setManufacturer(car.getManufacturer())
                .setModel(car.getModel())
                .setId(car.getId())
                .build());
    }

    @Override
    @Blocking
    @Transactional
    public Uni<CarResponse> remove(RemoveCarRequest request) {
        Optional<Car> optionalCar = carRepository
            .findByLicensePlateNumberOptional(
                request.getLicensePlateNumber());

        if (optionalCar.isPresent()) {
            Car removedCar = optionalCar.get();
            carRepository.delete(removedCar);
            return Uni.createFrom().item(CarResponse.newBuilder()
                .setLicensePlateNumber(removedCar.getLicensePlateNumber())
                .setManufacturer(removedCar.getManufacturer())
                .setModel(removedCar.getModel())
                .setId(removedCar.getId())
                .build());
        }
        return Uni.createFrom().nullItem();
    }
}
