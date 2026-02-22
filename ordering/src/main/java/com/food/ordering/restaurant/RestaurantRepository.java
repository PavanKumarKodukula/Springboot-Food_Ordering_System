package com.food.ordering.restaurant;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RestaurantRepository
        extends MongoRepository<Restaurant, String> {

    List<Restaurant> findByAddress_LocationNear(
            Point location,
            Distance distance
    );

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Restaurant> searchByName(String keyword);
}
