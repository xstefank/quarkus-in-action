package org.acme.inventory;

import java.util.Optional;

import javax.inject.Inject;

import com.google.protobuf.Empty;

import org.acme.inventory.database.CarInventory;
import org.acme.inventory.model.Car;
import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryService;
import org.acme.inventory.model.RemoveCarRequest;
import org.jboss.logging.Logger;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@GrpcService
public class InventoryServiceImpl implements InventoryService {

  private static final Logger LOGGER = Logger.getLogger(InventoryServiceImpl.class);

  @Inject
  CarInventory inventory;

  @Override
  public Uni<Empty> add(InsertCarRequest request) {
    Car car = new Car();
    car.licensePlateNumber = request.getLicensePlateNumber();
    car.manufacturer = request.getManufacturer();
    car.model = request.getModel();
    LOGGER.info("Persisting " + car);
    inventory.getCars().add(car);
    return Uni.createFrom().item(Empty.newBuilder().build());
  }


  @Override
  public Uni<Empty> remove(RemoveCarRequest request) {
    LOGGER.info("Removing car " + request.getLicensePlateNumber());
    Optional<Car> optionalCar = inventory.getCars().stream()
        .filter(car -> request.getLicensePlateNumber().equals(car.licensePlateNumber)).findFirst();
    optionalCar.ifPresent(car -> inventory.getCars().remove(car));
    return Uni.createFrom().item(Empty.newBuilder().build());
  }
}
