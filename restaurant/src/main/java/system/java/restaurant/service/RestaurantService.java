package system.java.restaurant.service;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import system.java.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    Restaurant addRestaurant(Restaurant restaurant);
    Page<Restaurant> getAllRestaurants(Pageable pageable);

    Restaurant getRestaurantById(Long id);

    Restaurant updateRestaurant(String id, Restaurant restaurantDetails);

    void deleteRestaurant(Long id);


    boolean isAvailable(Long restaurantId);
}
