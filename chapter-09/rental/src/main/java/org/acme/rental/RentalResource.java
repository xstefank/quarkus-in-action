package org.acme.rental;

import org.acme.rental.billing.Invoice;
import org.acme.rental.entity.Rental;
import org.acme.rental.reservation.Reservation;
import org.acme.rental.reservation.ReservationClient;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Path("/rental")
public class RentalResource {

    private static final double STANDARD_PRICE_FOR_PROLONGED_DAY = 25.99;

    @Inject
    @RestClient
    ReservationClient reservationClient;

    @Inject
    @Channel("prolong")
    Emitter<Invoice> prolongEmitter;

    @Path("/start/{userId}/{reservationId}/{carId}")
    @POST
    @Transactional
    public Rental start(Long userId, Long reservationId, Long carId) {
        Rental rental = new Rental();
        rental.userId = userId;
        rental.carId = carId;
        rental.reservationId = reservationId;
        rental.startDate = LocalDate.now();
        rental.active = true;

        rental.persist();

        return rental;
    }

    @PUT
    @Path("/end/{userId}/{reservationId}")
    @Transactional
    public Rental end(Long userId, Long reservationId) {
        Rental rental = Rental
            .findByUserAndReservationIdsOptional(userId, reservationId)
            .orElseThrow(() -> new NotFoundException("Rental not found"));

        Reservation reservation = reservationClient.getReservationById(reservationId);

        LocalDate today = LocalDate.now();
        if (reservation.endDay.isBefore(today)) {
            prolongEmitter.send(new Invoice(userId, rental, reservation.endDay, computePrice(reservation.endDay, today)));
        }
        rental.active = false;
        rental.endDate = today;
        rental.update();
        return rental;
    }

    private double computePrice(LocalDate endDate, LocalDate today) {
        return Duration.between(endDate.atStartOfDay(),
            today.atStartOfDay()).toDays() * STANDARD_PRICE_FOR_PROLONGED_DAY;
    }

    @GET
    public List<Rental> list() {
        return Rental.listAll();
    }

    @GET
    @Path("/active")
    public List<Rental> listActive() {
        return Rental.listActive();
    }

    @PUT
    @Path("prolong/{rentalId}")
    @Transactional
    public Rental prolongRental(@RestPath Long rentalId, int numberOfDays) {
        Rental rental = Rental.<Rental>findByIdOptional(rentalId)
            .orElseThrow(() -> new NotFoundException("Rental not found: " + rentalId));
        rental.endDate = rental.endDate.plusDays(numberOfDays);
        return rental;
    }
}
