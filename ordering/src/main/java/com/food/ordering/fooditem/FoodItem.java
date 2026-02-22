package com.food.ordering.fooditem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "food_items")
public class FoodItem {

    @Id
    private String id;

    private String name;
    private double price;
    private String restaurantId;
}
