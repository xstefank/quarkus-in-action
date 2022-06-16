package org.acme.inventory.client;

import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryServiceGrpc;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class GrpcInventoryClient implements QuarkusApplication {

  @GrpcClient("inventory")
  InventoryServiceGrpc.InventoryServiceBlockingStub inventory;

  @Override
  public int run(String... args) throws Exception {
    try {
    inventory.add(InsertCarRequest.newBuilder()
                         .setLicensePlateNumber(args[0])
                         .setManufacturer(args[1])
                         .setManufacturer(args[2])
                         .build());
    } catch (Exception e) {
      System.err.println("Error adding car: " + e.getMessage());
      return 1;
    }
    return 0;
  }
}
