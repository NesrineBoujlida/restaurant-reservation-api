package system.java.reservation.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import system.java.reservation.model.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    Page<Reservation> getAllReservations(Pageable pageable);
    boolean cancelReservation(Long id);
    Reservation getReservationById(Long id);
}