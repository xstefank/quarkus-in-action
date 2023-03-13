package org.acme.inventory.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.inventory.model.Car;
import org.acme.inventory.model.CarResponse;
import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryService;
import org.acme.inventory.model.RemoveCarRequest;
import org.acme.inventory.repository.CarRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@GrpcService
public class GrpcInventoryService
    implements InventoryService {

    @Inject
    CarRepository carRepository;

    @Override
    @Blocking
    @Transactional
    public Multi<CarResponse> add(Multi<InsertCarRequest> requests) {
        return requests
            .map(request -> {
                Car car = new Car();
                car.licensePlateNumber = request.getLicensePlateNumber();
                car.manufacturer = request.getManufacturer();
                car.model = request.getModel();
                return car;
            }).onItem().invoke(car -> {
                Log.info("Persisting " + car);
                carRepository.persist(car);
            }).map(car -> CarResponse.newBuilder()
                .setLicensePlateNumber(car.licensePlateNumber)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setId(car.id)
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
                .setLicensePlateNumber(removedCar.licensePlateNumber)
                .setManufacturer(removedCar.manufacturer)
                .setModel(removedCar.model)
                .setId(removedCar.id)
                .build());
        }
        return Uni.createFrom().nullItem();
    }
}
