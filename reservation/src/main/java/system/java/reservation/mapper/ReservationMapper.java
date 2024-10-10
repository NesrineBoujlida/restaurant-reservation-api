package system.java.reservation.mapper;


import org.springframework.stereotype.Component;
import system.java.reservation.dto.ReservationDTO;
import system.java.reservation.model.Reservation;

@Component
public class ReservationMapper {

    public ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setRestaurantId(reservation.getRestaurantId());
        dto.setGuestId(reservation.getGuestId());
        dto.setReservationTime(reservation.getReservationTime());
        return dto;
    }

    public Reservation toEntity(ReservationDTO reservationDTO) {
        if (reservationDTO == null) {
            throw new IllegalArgumentException("ReservationDTO must not be null");
        }

        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());

        // Check if restaurantId and guestId are set
        if (reservationDTO.getRestaurantId() == null) {
            throw new IllegalArgumentException("Restaurant ID must not be null");
        }
        if (reservationDTO.getGuestId() == null) {
            throw new IllegalArgumentException("Guest ID must not be null");
        }

        reservation.setRestaurantId(reservationDTO.getRestaurantId());
        reservation.setGuestId(reservationDTO.getGuestId());
        reservation.setReservationTime(reservationDTO.getReservationTime());
        return reservation;
    }

}

