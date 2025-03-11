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
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
//@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;
    private final Cart cart;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService , OrderService orderService, Cart cart) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderService = orderService;
        this.cart = cart;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User entered cannot  be null");
        }
        if (userRepository.getUserById(user.getId()) != null) {
            throw new IllegalStateException("User with ID " + user.getId() + " already exists.");
        }

        User savedUser = userRepository.addUser(user);
        Cart newCart = new Cart(savedUser.getId());
        cartService.addCart(newCart);
        return savedUser;
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return user;
    }


    public List<Order> getOrdersByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userRepository.getUserById(userId) == null) {
            throw new NoSuchElementException("User not found");
        }
        return userRepository.getOrdersByUserId(userId);
    }


    public void addOrderToUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userRepository.getUserById(userId) == null) {
            throw new NoSuchElementException("User not found");
        }
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null ) {
            throw new IllegalStateException("no cart");
        }
        if ( cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot place order.");
        }

        double totalPrice = cart.getProducts().stream().mapToDouble(Product::getPrice).sum();

        Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, new ArrayList<>(cart.getProducts()));

        //System.out.println("New Order Created: " + newOrder);

        orderService.addOrder(newOrder);

        //System.out.println("Order should be saved now. Checking userRepository...");

        userRepository.addOrderToUser(userId, newOrder);

        //System.out.println("Order added to user successfully!");

        emptyCart(userId);
    }

    public void emptyCart(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (userRepository.getUserById(userId) == null) {
            throw new NoSuchElementException("User not found");
        }

        cartService.emptyCart(userId);
    }


    public void removeOrderFromUser(UUID userId, UUID orderId) {
        if (userId == null || orderId == null) {
            throw new IllegalArgumentException("User ID and Order ID cannot be null");
        }

        if (userRepository.getUserById(userId) == null) {
            throw new NoSuchElementException("User not found");
        }

        List<Order> orders = getOrdersByUserId(userId);
        if (orders.stream().noneMatch(order -> order.getId().equals(orderId))) {
            throw new NoSuchElementException("Order not found for the user");
        }

        userRepository.removeOrderFromUser(userId, orderId);
        orderService.deleteOrderById(orderId);
    }


    public void deleteUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (userRepository.getUserById(userId) == null) {
            throw new NoSuchElementException("User not found");
        }

        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            cartService.deleteCartById(cart.getId());
        }

        List<Order> orders = getOrdersByUserId(userId);
        for (Order order : orders) {
            orderService.deleteOrderById(order.getId());
        }

        userRepository.deleteUserById(userId);
    }


}
