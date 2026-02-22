package com.food.ordering.order;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/food-item/{foodItemId}")
    public List<Order> getOrdersByFoodItem(
            @PathVariable String foodItemId) {
        return orderService.findOrdersByFoodItem(foodItemId);
    }

    @GetMapping("/search")
    public List<Order> searchOrders(
            @RequestParam(required = false) String restaurantId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount
    ) {
        return orderService.searchOrders(
                restaurantId, userId, minAmount, maxAmount
        );
    }

    @GetMapping("/latest")
    public Page<Order> getLatestOrders(
            @RequestParam int page) {
        return orderService.getLatestOrders(page);
    }
}