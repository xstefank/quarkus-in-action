package org.acme.rental;

import org.acme.rental.entity.Rental;
import org.acme.rental.repository.RentalRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Path("/rental")
public class RentalResource {

    @Inject
    RentalRepository rentalRepository;

    @Path("/start/{userId}/{reservationId}")
    @POST
    @Transactional
    public Rental start(Long userId, Long reservationId) {
        Rental rental = new Rental();
        rental.setUserId(userId);
        rental.setReservationId(reservationId);
        rental.setStartDate(LocalDate.now());
        rental.setActive(true);

        rentalRepository.persist(rental);

        return rental;
    }

    @PUT
    @Path("/end/{userId}/{reservationId}")
    @Transactional
    public Rental end(Long userId, Long reservationId) {
        Optional<Rental>optionalRental = rentalRepository
            .find("userId = ?1 and reservationId = ?2",
                userId, reservationId)
            .firstResultOptional();

        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.setActive(false);
            rental.setEndDate(LocalDate.now());

            return rental;
        } else {
            throw new NotFoundException("Rental not found");
        }
    }

    @GET
    public List<Rental> listRentals(
        @QueryParam("active") @DefaultValue("false") boolean active) {
        return active ? rentalRepository.listActive() : rentalRepository.listAll();
    }
}
