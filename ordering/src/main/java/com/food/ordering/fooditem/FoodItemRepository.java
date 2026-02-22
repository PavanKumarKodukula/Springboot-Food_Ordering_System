package com.food.ordering.fooditem;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FoodItemRepository
        extends MongoRepository<FoodItem, String> {

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<FoodItem> searchByName(String keyword);
}