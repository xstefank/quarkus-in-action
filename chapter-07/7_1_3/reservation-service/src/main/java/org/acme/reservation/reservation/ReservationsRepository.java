package org.acme.reservation.reservation;

import org.acme.reservation.entity.Reservation;

import java.util.List;

public interface ReservationsRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);
}
