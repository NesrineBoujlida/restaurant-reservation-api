package system.java.restaurant.serviceImpl;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.springframework.web.server.ResponseStatusException;
import system.java.restaurant.model.Restaurant;
import system.java.restaurant.repository.RestaurantRepository;
import system.java.restaurant.service.RestaurantService;

import java.util.Optional;


@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }



    @Override
    public Restaurant getRestaurantById(Long id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        if (restaurantOptional.isPresent()) {
            return restaurantOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
    }


    @Override
    public Restaurant updateRestaurant(String id, Restaurant restaurantDetails) {
        return null;
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public boolean isAvailable(Long restaurantId) {
        return true;
    }
}
