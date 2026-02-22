package com.food.ordering.restaurant;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.createRestaurant(restaurant);
    }

    @PutMapping("/{id}")
    public Restaurant updateRestaurant(
            @PathVariable String id,
            @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        return restaurantService.updateRestaurant(restaurant);
    }

    @DeleteMapping("/{id}")
    public void deleteRestaurant(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
    }

    @GetMapping("/nearby")
    public List<Restaurant> getNearbyRestaurants(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusKm) {

        return restaurantService.findNearbyRestaurants(
                latitude, longitude, radiusKm
        );
    }

    @GetMapping("/nearby/user/{userId}")
    public List<Restaurant> getNearbyRestaurantsByUser(
            @PathVariable String userId,
            @RequestParam double radiusKm) {

        return restaurantService.findNearbyRestaurantsByUser(
                userId, radiusKm
        );
    }

    @GetMapping("/search")
    public List<Restaurant> searchRestaurantByName(
            @RequestParam String keyword) {

        return restaurantService.searchRestaurants(keyword);
    }
}
