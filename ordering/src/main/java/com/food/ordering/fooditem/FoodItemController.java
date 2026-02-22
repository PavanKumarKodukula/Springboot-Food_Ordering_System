package com.food.ordering.fooditem;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/food-items")
public class FoodItemController {

    private final FoodItemService service;

    public FoodItemController(FoodItemService service) {
        this.service = service;
    }

    @PostMapping
    public FoodItem createFoodItem(@RequestBody FoodItem foodItem) {
        return service.createFoodItem(foodItem);
    }

    @GetMapping("/search")
    public List<FoodItem> searchFoodItems(
            @RequestParam String keyword) {
        return service.searchFoodItems(keyword);
    }

    @GetMapping("/filter")
    public List<FoodItem> filterFoodItems(
            @RequestParam(required = false) String restaurantId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        return service.filterFoodItems(
                restaurantId, minPrice, maxPrice
        );
    }

    @PutMapping("/{id}")
    public FoodItem updateFoodItem(
            @PathVariable String id,
            @RequestBody FoodItem foodItem) {
        foodItem.setId(id);
        return service.updateFoodItem(foodItem);
    }

    @DeleteMapping("/{id}")
    public void deleteFoodItem(@PathVariable String id) {
        service.deleteFoodItem(id);
    }
}