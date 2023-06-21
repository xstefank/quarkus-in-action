package org.acme.reservation.rest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.mutiny.Uni;
import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;
import org.acme.reservation.inventory.InventoryClient;
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
    SecurityContext context;

    public ReservationResource(
                               @GraphQLClient("inventory")
                               GraphQLInventoryClient inventoryClient,
                               @RestClient RentalClient rentalClient) {
        this.inventoryClient = inventoryClient;
        this.rentalClient = rentalClient;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @ReactiveTransactional
    public Uni<Reservation> make(Reservation reservation) {
        reservation.userId = context.getUserPrincipal() != null ?
            context.getUserPrincipal().getName() : "anonymous";
        return reservation.<Reservation>persist().onItem()
            .call(persistedReservation -> {
                Log.info("Successfully reserved reservation " + persistedReservation);
                if (persistedReservation.startDay.equals(LocalDate.now())) {
                    return rentalClient.start(persistedReservation.userId,
                            persistedReservation.id)
                        .onItem().invoke(rental ->
                            Log.info("Successfully started rental " + rental))
                        .replaceWith(persistedReservation);

                }
                return Uni.createFrom().item(persistedReservation);
            });
    }

    @GET
    @Path("all")
    public Uni<List<Reservation>> allReservations() {
        String userId = context.getUserPrincipal() != null ?
            context.getUserPrincipal().getName() : null;
        return Reservation.<Reservation>streamAll()
            .filter(reservation -> userId == null ||
                userId.equals(reservation.userId))
            .collect().asList();
    }

    @GET
    @Path("availability")
    public Uni<Collection<Car>> availability(@RestQuery LocalDate startDate,
                                        @RestQuery LocalDate endDate) {
        Uni<Map<Long, Car>> carsUni = inventoryClient.allCars()
            .map(cars -> cars.stream().collect(Collectors
                .toMap(car -> car.id, Function.identity())));

        Uni<List<Reservation>> reservationsUni = Reservation.listAll();

        return Uni.combine().all()
            .unis(carsUni, reservationsUni)
            .asTuple()
            .chain(tuple -> {
                Map<Long, Car> carsById = tuple.getItem1();
                List<Reservation> reservations = tuple.getItem2();

                // for each reservation, remove the car from the map
                for (Reservation reservation : reservations) {
                    if (reservation.isReserved(startDate, endDate)) {
                        carsById.remove(reservation.carId);
                    }
                }
                return Uni.createFrom().item(carsById.values());
            });
    }
}
