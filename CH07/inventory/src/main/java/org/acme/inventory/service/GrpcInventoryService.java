
package org.acme.inventory.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.inventory.model.Car;
import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryService;
import org.acme.inventory.model.RemoveCarRequest;
import org.acme.inventory.repository.CarRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@GrpcService
public class GrpcInventoryService implements InventoryService {

    private static final Logger LOGGER = Logger.getLogger(GrpcInventoryService.class);

    @Inject
    CarRepository carRepository;

    @Override
    @Blocking
    @Transactional
    public Uni<Empty> remove(RemoveCarRequest request) {
        Optional<Car> optionalCar = carRepository.findByLicensePlateNumberOptional(request.getLicensePlateNumber());
        optionalCar.ifPresent(car -> carRepository.delete(car));
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    @Blocking
    @Transactional
    public Uni<Empty> add(Multi<InsertCarRequest> request) {
        return request
            .map(r -> {
                Car car = new Car();
                car.licensePlateNumber = r.getLicensePlateNumber();
                car.manufacturer = r.getManufacturer();
                car.model = r.getModel();
                LOGGER.error("Persisting " + car);
                return car;
            })
            .onItem().invoke(car -> carRepository.persist(car))
            .collect().last()
            .map(l -> Empty.newBuilder().build());
    }
}
