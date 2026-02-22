package com.food.ordering.order;

import com.food.ordering.restaurant.Restaurant;
import com.food.ordering.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private User user;
    private Restaurant restaurant;

    private List<OrderItem> orderItems;

    private double totalAmount;

    private LocalDateTime createdAt;
}
