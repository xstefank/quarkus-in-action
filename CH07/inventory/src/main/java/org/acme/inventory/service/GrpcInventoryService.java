
package org.acme.inventory.service;

import javax.inject.Inject;

import com.google.protobuf.Empty;

import org.acme.inventory.model.Car;
import org.acme.inventory.database.CarInventory;
import org.acme.inventory.model.InventoryServiceGrpc;
import org.acme.inventory.model.InsertCarRequest;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import org.jboss.logging.Logger;

@GrpcService
public class GrpcInventoryService extends InventoryServiceGrpc.InventoryServiceImplBase {

  private static final Logger LOGGER = Logger.getLogger(GrpcInventoryService.class);

  @Inject
  CarInventory inventory;

  @Override
    public StreamObserver<InsertCarRequest> add(StreamObserver<Empty> responseObserver) {
    return new io.grpc.stub.StreamObserver<InsertCarRequest>() {
        @Override
        public void onNext(InsertCarRequest request) {
          Car car = new Car();
          car.licensePlateNumber = request.getLicensePlateNumber();
          car.manufacturer = request.getManufacturer();
          car.model = request.getModel();
          inventory.getCars().add(car);
        }

        @Override
        public void onCompleted() {
          responseObserver.onNext(Empty.newBuilder().build());
          responseObserver.onCompleted();
        }

        @Override
        public void onError(Throwable t) {
          LOGGER.error(t.getMessage());
        }
    };
  }
}
