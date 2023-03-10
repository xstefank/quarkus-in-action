package org.acme.reservation.rest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.logging.Log;
import io.smallrye.graphql.client.GraphQLClient;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;
import org.acme.reservation.inventory.InventoryClient;
import org.acme.reservation.rental.Rental;
import org.acme.reservation.rental.RentalClient;
import org.acme.reservation.entity.Reservation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestQuery;

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private final InventoryClient inventoryClient;
    private final RentalClient rentalClient;

    @Inject
    javax.ws.rs.core.SecurityContext context;

    public ReservationResource(
                               @GraphQLClient("inventory")
                               GraphQLInventoryClient inventoryClient,
                               @RestClient RentalClient rentalClient) {
        this.inventoryClient = inventoryClient;
        this.rentalClient = rentalClient;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Transactional // <1>
    public Reservation make(Reservation reservation) {
        reservation.userId = context.getUserPrincipal() != null ?
            context.getUserPrincipal().getName() : null;
        reservation.persist(); // <2>
        Log.info("Successfully reserved reservation " + reservation);
        if (reservation.startDay.equals(LocalDate.now())) {
            Rental rental = rentalClient
                .start(reservation.userId, reservation.id);// <3>
            Log.info("Successfully started rental " + rental);
        }
        return reservation;
    }

    @GET
    @Path("all")
    public Collection<Reservation> allReservations() {
        String userId = context.getUserPrincipal() != null ?
            context.getUserPrincipal().getName() : null;
        return Reservation.<Reservation>streamAll()
            .filter(reservation -> userId == null ||
                userId.equals(reservation.userId))
        .collect(Collectors.toList());
    }

    @GET
    @Path("availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate,
                                        @RestQuery LocalDate endDate) {
        // obtain all cars from inventory
        List<Car> availableCars = inventoryClient.allCars();
        // create a map from id to car
        Map<Long, Car> carsById = new HashMap<>();
        for (Car car : availableCars) {
            carsById.put(car.id, car);
        }

        // get all current reservations
        List<Reservation> reservations = Reservation.listAll();
        // for each reservation, remove the car from the map
        for (Reservation reservation : reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsById.remove(reservation.carId);
            }
        }
        return carsById.values();
    }
}
