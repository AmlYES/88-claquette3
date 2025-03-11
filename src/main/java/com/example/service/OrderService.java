package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {
    // The Dependency Injection Variables
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    public OrderService(OrderRepository orderRepository) {
        super();
        this.orderRepository = orderRepository;
    }

    // The Constructor with the required variables mapping the Dependency Injection
//    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
//        this.orderRepository = orderRepository;
//        this.userRepository = userRepository;
//    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        if (order.getUserId() == null) {
            throw new IllegalStateException("Order must have an associated user.");
        }
        if (orderRepository.getOrderById(order.getId()) != null) {
            throw new IllegalStateException("Order with ID " + order.getId() + " already exists.");
        }
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null.");
        }
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalStateException("Order with ID " + orderId + " not found.");
        }
        return order;
    }

    public void deleteOrderById(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null.");
        }

        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalStateException("Order not found");
        }

        orderRepository.deleteOrderById(orderId);

        // Ensure user exists before trying to remove order from them
        if (userRepository.getUserById(order.getUserId()) != null) {
            userRepository.removeOrderFromUser(order.getUserId(), orderId);
        }
    }

}
