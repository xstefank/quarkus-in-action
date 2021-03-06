package org.acme.reservation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.mutiny.Uni;
import org.acme.reservation.entity.Reservation;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;
import org.acme.reservation.inventory.InventoryClient;
import org.acme.reservation.rental.Rental;
import org.acme.reservation.rental.RentalClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private static final Logger LOGGER = Logger.getLogger(ReservationResource.class);

    private final InventoryClient inventoryClient;

    private final RentalClient rentalClient;

    public ReservationResource(@GraphQLClient("inventory") GraphQLInventoryClient inventoryClient,
                               @RestClient RentalClient rentalClient) {
        this.inventoryClient = inventoryClient;
        this.rentalClient = rentalClient;
    }

    @GET
    @Path("availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
        // obtain all cars from inventory 
        List<Car> availableCars = inventoryClient.allCars();
        // create a map from id to car
        Map<Long, Car> carsById = new HashMap<>();
        for (Car car : availableCars) {
            carsById.put(car.id, car);
        }

        // get all current reservations
        // not the intended way with reactive types, we block here for rewrite simplicity
        List<Reservation> reservations = Reservation.<Reservation>listAll().await().indefinitely();
        // for each reservation, remove the car from the map
        for (Reservation reservation : reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsById.remove(reservation.carId);
            }
        }
        return carsById.values();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @ReactiveTransactional
    public Uni<Reservation> make(Reservation reservation) {
        return reservation.<Reservation>persist().onItem()
            .invoke(persistedReservation -> {
                LOGGER.info("Successfully reserved reservation " + persistedReservation);
                Long userId = 1L;
                if (persistedReservation.startDay.equals(LocalDate.now())) {
                    // start the rental at the Rental service
                    Rental rental = rentalClient.start(userId, persistedReservation.id);
                    LOGGER.info("Successfully started rental " + rental);
                }
            });
    }

    @GET
    public Uni<List<Reservation>> getReservations() {
        return Reservation.listAll();
    }
}
