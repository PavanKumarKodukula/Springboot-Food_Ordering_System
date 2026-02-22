package com.food.ordering.order;

import com.food.ordering.fooditem.FoodItem;
import com.food.ordering.fooditem.FoodItemRepository;
import com.food.ordering.restaurant.Restaurant;
import com.food.ordering.restaurant.RestaurantRepository;
import com.food.ordering.user.User;
import com.food.ordering.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    public OrderService(OrderRepository orderRepository,
                        MongoTemplate mongoTemplate,
                        UserRepository userRepository,
                        RestaurantRepository restaurantRepository,
                        FoodItemRepository foodItemRepository) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public Order placeOrder(Order order) {

        User requestUser = order.getUser();

        User dbUser = userRepository.findById(requestUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!dbUser.getName().equals(requestUser.getName()) ||
                !dbUser.getEmail().equals(requestUser.getEmail())) {
            throw new RuntimeException("User details mismatch");
        }

        Restaurant requestRestaurant = order.getRestaurant();

        Restaurant dbRestaurant = restaurantRepository.findById(requestRestaurant.getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!dbRestaurant.getName().equals(requestRestaurant.getName())) {
            throw new RuntimeException("Restaurant details mismatch");
        }

        double total = 0;

        for (OrderItem item : order.getOrderItems()) {

            FoodItem dbItem = foodItemRepository.findById(
                    item.getFoodItem().getId()
            ).orElseThrow(() ->
                    new RuntimeException("Food item not found")
            );

            if (!dbItem.getRestaurantId().equals(dbRestaurant.getId())) {
                throw new RuntimeException("Food item does not belong to restaurant");
            }

            if (dbItem.getPrice() != item.getFoodItem().getPrice()) {
                throw new RuntimeException("Food item price mismatch");
            }

            total += dbItem.getPrice() * item.getQuantity();
        }

        order.setTotalAmount(total);
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    public List<Order> searchOrders(
            String restaurantId,
            String userId,
            Double minAmount,
            Double maxAmount
    ) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (restaurantId != null && !restaurantId.isBlank()) {
            criteriaList.add(Criteria.where("restaurant.id").is(restaurantId));
        }

        if (userId != null && !userId.isBlank()) {
            criteriaList.add(Criteria.where("user.id").is(userId));
        }

        if (minAmount != null) {
            criteriaList.add(Criteria.where("totalAmount").gte(minAmount));
        }

        if (maxAmount != null) {
            criteriaList.add(Criteria.where("totalAmount").lte(maxAmount));
        }

        Criteria criteria = new Criteria();

        if (!criteriaList.isEmpty()) {
            criteria = new Criteria().andOperator(
                    criteriaList.toArray(new Criteria[0])
            );
        }

        return mongoTemplate.find(new Query(criteria), Order.class);
    }

    public List<Order> findOrdersByFoodItem(String foodItemId) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (foodItemId != null && !foodItemId.isBlank()) {
            criteriaList.add(
                    Criteria.where("orderItems.foodItem.id").is(foodItemId)
            );
        }

        Criteria criteria = new Criteria();

        if (!criteriaList.isEmpty()) {
            criteria = new Criteria().andOperator(
                    criteriaList.toArray(new Criteria[0])
            );
        }

        return mongoTemplate.find(new Query(criteria), Order.class);
    }

    public Page<Order> getLatestOrders(int page) {

        Pageable pageable = PageRequest.of(
                page,
                10,
                Sort.by("createdAt").descending()
        );

        return orderRepository.findAll(pageable);
    }
}