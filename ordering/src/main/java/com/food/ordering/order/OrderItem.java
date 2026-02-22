package com.food.ordering.order;

import com.food.ordering.fooditem.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private FoodItem foodItem;
    private int quantity;
}
