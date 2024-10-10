package system.java.reservation.dto;

import java.time.LocalDateTime;


public class ReservationDTO {
    private Long id;
    private Long restaurantId;
    private Long guestId;
    private LocalDateTime reservationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", guestId=" + guestId +
                ", reservationTime=" + reservationTime +
                '}';
    }
}

