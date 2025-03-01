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
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
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
        // Step 1: Get the user's cart
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot place order.");
        }
        // Step 2: Calculate total price
        double totalPrice = cart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        // Step 3: Create a new order

        Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, new ArrayList<>(cart.getProducts()));
        //orderService.addOrder(Order order);
        // Step 4: Add the new order to the user's order list
        userRepository.addOrderToUser(userId, newOrder);

        // Step 5: Empty the user's cart
        emptyCart(userId);
    }
    public void emptyCart(UUID userId) {
        cartService.emptyCart(userId);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
    }
}
