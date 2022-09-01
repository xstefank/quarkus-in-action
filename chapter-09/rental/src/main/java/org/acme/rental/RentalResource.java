package org.acme.rental;

import org.acme.rental.entity.Rental;
import org.jboss.resteasy.reactive.RestPath;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Path("/rental")
public class RentalResource {

    @Path("/start/{userId}/{reservationId}")
    @POST
    @Transactional
    public Rental start(Long userId, Long reservationId) {
        Rental rental = new Rental();
        rental.userId = userId;
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
        Optional<Rental> optionalRental = Rental
            .findByUserAndReservationIdsOptional(userId, reservationId);

        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.endDate = LocalDate.now();
            rental.active = false;
            rental.update();
            return rental;
        } else {
            throw new NotFoundException("Rental not found");
        }
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
