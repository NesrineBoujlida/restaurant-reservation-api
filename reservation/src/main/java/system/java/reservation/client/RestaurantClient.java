package system.java.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {
    @GetMapping("/restaurants/availability/{restaurantId}")
    Boolean checkAvailability(@PathVariable("restaurantId") Long restaurantId);

}
