package system.java.reservation.serviceImpl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import system.java.reservation.client.RestaurantClient;
import system.java.reservation.model.Reservation;
import system.java.reservation.repository.ReservationRepository;
import system.java.reservation.service.ReservationService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private RestaurantClient restaurantClient;

    @Autowired
    ReservationRepository reservationRepository;


    @Override
    public Reservation createReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation entity must not be null");
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Override
    public boolean cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        reservationRepository.delete(reservation);
        return true;
    }


    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }



}
