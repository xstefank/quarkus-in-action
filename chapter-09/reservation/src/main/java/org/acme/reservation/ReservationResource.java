package org.acme.reservation;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;
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
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.acme.reservation.billing.Invoice;
import org.acme.reservation.entity.Reservation;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;
import org.acme.reservation.inventory.InventoryClient;
import org.acme.reservation.rental.Rental;
import org.acme.reservation.rental.RentalClient;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
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

    private static final double STANDARD_RATE_PER_DAY = 19.99;

    @Inject
    @Channel("invoices")
    MutinyEmitter<Invoice> invoiceEmitter;

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @ReactiveTransactional
    public Uni<Reservation> make(Reservation reservation) {
        Long userId = 1L;
        Uni<Void> invoiceUni = invoiceEmitter.send(new Invoice(userId, reservation, computePrice(reservation)))
            .onItem().invoke(() -> reservation.paid = true)
            .onFailure().recoverWithUni(Uni.createFrom().nullItem());
        Uni<Reservation> persistUni = reservation.persist();

        return Uni.combine().all().unis(invoiceUni, persistUni)
            .combinedWith((invoiceAck, persistedReservation) -> {
                LOGGER.info("Successfully reserved reservation " + persistedReservation);
                return persistedReservation;
            })
                .onItem().call(persistedReservation -> {
                if (persistedReservation.startDay.equals(LocalDate.now()) && reservation.paid) {
                    // start the rental at the Rental service
                    return rentalClient.start(userId, persistedReservation.id)
                        .onItem().invoke(response -> LOGGER.info("Successfully started rental " + response))
                        .replaceWith(persistedReservation);
                }
                return Uni.createFrom().item(persistedReservation);
            });
    }

    private double computePrice(Reservation reservation) {
        return Duration.between(reservation.startDay.atStartOfDay(),
            reservation.endDay.atStartOfDay()).toDays() * STANDARD_RATE_PER_DAY;
    }

    @GET
    public Uni<List<Reservation>> getReservations() {
        return Reservation.listAll();
    }
}
