package org.acme.reservation.database;

import java.util.Collections;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.acme.reservation.Reservation;

@ApplicationScoped
public class Reservations {

    public List<Reservation> all() {
        return Collections.emptyList();
    }
}
