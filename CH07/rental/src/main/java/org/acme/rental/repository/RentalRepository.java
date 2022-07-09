package org.acme.rental.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.rental.entity.Rental;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RentalRepository implements PanacheRepository<Rental> {

    public List<Rental> listActive() {
        return list("active", true);
    }
}
