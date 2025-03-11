package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.Order;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service

public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService , OrderService orderService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public User addUser(User user) {
        User savedUser = userRepository.addUser(user);
        Cart newCart = new Cart(savedUser.getId());
        cartService.addCart(newCart);
        return savedUser;
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot place order.");
        }

        double totalPrice = cart.getProducts().stream().mapToDouble(Product::getPrice).sum();

        Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, new ArrayList<>(cart.getProducts()));

        System.out.println("New Order Created: " + newOrder);

        orderService.addOrder(newOrder);

        System.out.println("Order should be saved now. Checking userRepository...");

        userRepository.addOrderToUser(userId, newOrder);

        System.out.println("Order added to user successfully!");

        emptyCart(userId);
    }

    public void emptyCart(UUID userId) {
        cartService.emptyCart(userId);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
        orderService.deleteOrderById(orderId);
    }

    public void deleteUserById(UUID userId) {
        // Delete the cart linked to this user (if exists)
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            cartService.deleteCartById(cart.getId());
        }
        // Delete all orders linked to this user
        List<Order> orders = getOrdersByUserId(userId);
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                orderService.deleteOrderById(order.getId());
            }
        }

        userRepository.deleteUserById(userId);
    }
}
