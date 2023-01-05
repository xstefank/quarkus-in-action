package org.acme.inventory.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import org.acme.inventory.database.CarInventory;
import org.acme.inventory.model.Car;
import org.acme.inventory.model.CarResponse;
import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryService;
import org.acme.inventory.model.RemoveCarRequest;

import javax.inject.Inject;
import java.util.Optional;

@GrpcService
public class InventoryServiceImpl
    implements InventoryService {

    @Inject
    CarInventory inventory;

    @Override
    public Uni<CarResponse> add(
        InsertCarRequest request) {
        Car car = new Car();
        car.licensePlateNumber = request.getLicensePlateNumber();
        car.manufacturer = request.getManufacturer();
        car.model = request.getModel();
        Log.info("Persisting " + car);
        inventory.getCars().add(car);

        return Uni.createFrom().item(CarResponse.newBuilder()
            .setLicensePlateNumber(car.licensePlateNumber)
            .setManufacturer(car.manufacturer)
            .setModel(car.model)
            .build());
    }

    @Override
    public Uni<CarResponse> remove(
        RemoveCarRequest request) {
        Optional<Car> optionalCar = inventory.getCars().stream()
        .filter(car -> request.getLicensePlateNumber()
            .equals(car.licensePlateNumber))
        .findFirst();

        if (optionalCar.isPresent()) {
            Car removedCar = optionalCar.get();
            inventory.getCars().remove(removedCar);
            return Uni.createFrom().item(CarResponse.newBuilder()
                .setLicensePlateNumber(removedCar.licensePlateNumber)
                .setManufacturer(removedCar.manufacturer)
                .setModel(removedCar.model)
                .build());
        }
        return Uni.createFrom().nullItem();
    }
}
