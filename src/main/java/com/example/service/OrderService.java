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

    // The Constructor with the required variables mapping the Dependency Injection
//    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
//        this.orderRepository = orderRepository;
//        this.userRepository = userRepository;
//    }

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public void deleteOrderById(UUID orderId) throws IllegalArgumentException {
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
        orderRepository.deleteOrderById(orderId);
        userRepository.removeOrderFromUser(order.getUserId(), orderId);
    }
}
