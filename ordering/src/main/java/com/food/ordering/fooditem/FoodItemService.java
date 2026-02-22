package com.food.ordering.fooditem;

import com.food.ordering.restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodItemService {

    private final FoodItemRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final MongoTemplate mongoTemplate;

    public FoodItemService(FoodItemRepository repository,
                           RestaurantRepository restaurantRepository,
                           MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public FoodItem createFoodItem(FoodItem foodItem) {

        if (!restaurantRepository.existsById(foodItem.getRestaurantId())) {
            throw new RuntimeException("Restaurant not found for FoodItem");
        }

        return repository.save(foodItem);
    }

    public List<FoodItem> searchFoodItems(String keyword) {
        return repository.searchByName(keyword);
    }

    public List<FoodItem> filterFoodItems(
            String restaurantId,
            Double minPrice,
            Double maxPrice) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (restaurantId != null && !restaurantId.isBlank()) {
            criteriaList.add(
                    Criteria.where("restaurantId").is(restaurantId)
            );
        }

        if (minPrice != null) {
            criteriaList.add(
                    Criteria.where("price").gte(minPrice)
            );
        }

        if (maxPrice != null) {
            criteriaList.add(
                    Criteria.where("price").lte(maxPrice)
            );
        }

        Criteria criteria = new Criteria();

        if (!criteriaList.isEmpty()) {
            criteria = new Criteria().andOperator(
                    criteriaList.toArray(new Criteria[0])
            );
        }

        return mongoTemplate.find(new Query(criteria), FoodItem.class);
    }

    public FoodItem updateFoodItem(FoodItem foodItem) {

        if (!restaurantRepository.existsById(foodItem.getRestaurantId())) {
            throw new RuntimeException("Restaurant not found for FoodItem");
        }

        return repository.save(foodItem);
    }

    public void deleteFoodItem(String id) {
        repository.deleteById(id);
    }
}