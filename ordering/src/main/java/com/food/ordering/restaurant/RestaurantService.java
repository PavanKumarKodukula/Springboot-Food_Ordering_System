package com.food.ordering.restaurant;

import com.food.ordering.common.GeoLocation;
import com.food.ordering.user.User;
import com.food.ordering.user.UserRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        setGeoLocation(restaurant);
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        setGeoLocation(restaurant);
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }

    public List<Restaurant> findNearbyRestaurants(
            double latitude,
            double longitude,
            double radiusKm) {

        Point userPoint = new Point(longitude, latitude);
        Distance distance = new Distance(radiusKm, Metrics.KILOMETERS);

        return restaurantRepository.findByAddress_LocationNear(
                userPoint, distance
        );
    }

    public List<Restaurant> findNearbyRestaurantsByUser(
            String userId,
            double radiusKm) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Point userPoint = new Point(
                user.getAddress().getLongitude(),
                user.getAddress().getLatitude()
        );

        Distance distance = new Distance(radiusKm, Metrics.KILOMETERS);

        return restaurantRepository.findByAddress_LocationNear(
                userPoint, distance
        );
    }

    public List<Restaurant> searchRestaurants(String keyword) {
        return restaurantRepository.searchByName(keyword);
    }

    private void setGeoLocation(Restaurant restaurant) {

        if (restaurant.getAddress() == null) {
            return;
        }

        double lat = restaurant.getAddress().getLatitude();
        double lon = restaurant.getAddress().getLongitude();

        restaurant.getAddress().setLocation(
                new GeoLocation("Point", new double[]{lon, lat})
        );
    }
}
