package system.java.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.java.restaurant.model.Restaurant;
import system.java.restaurant.service.RestaurantService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Add a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public EntityModel<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.addRestaurant(restaurant);
        return toHateoasEntityModel(createdRestaurant);
    }

    @Operation(summary = "Retrieve a restaurant by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public EntityModel<Restaurant> getRestaurant(@PathVariable Long id) {
        System.out.println("iii . "+ id);
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return toHateoasEntityModel(restaurant);
    }

    @Operation(summary = "Retrieve all restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No restaurants found")
    })
    @GetMapping
    public List<EntityModel<Restaurant>> getAllRestaurants(Pageable pageable) {
        Page<Restaurant> restaurants = restaurantService.getAllRestaurants(pageable);
        return restaurants.stream()
                .map(this::toHateoasEntityModel)
                .toList();
    }

    @Operation(summary = "Check availability for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked successfully."),
            @ApiResponse(responseCode = "404", description = "Restaurant not found.")
    })
    @GetMapping("/availability/{restaurantId}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long restaurantId) {
        boolean isAvailable = restaurantService.isAvailable(restaurantId);
        return ResponseEntity.ok(isAvailable);
    }


    @Operation(summary = "Update an existing restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PutMapping("/{id}")
    public EntityModel<Restaurant> updateRestaurant(@PathVariable String id, @RequestBody Restaurant restaurantDetails) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, restaurantDetails);
        return toHateoasEntityModel(updatedRestaurant);
    }

    @Operation(summary = "Delete a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Restaurant> toHateoasEntityModel(Restaurant restaurant) {
        Link selfLink = linkTo(methodOn(RestaurantController.class).getRestaurant(restaurant.getId())).withSelfRel();
        Link allRestaurantsLink = linkTo(methodOn(RestaurantController.class).getAllRestaurants(Pageable.unpaged())).withRel("all-restaurants");
        return EntityModel.of(restaurant, selfLink, allRestaurantsLink);
    }
}
