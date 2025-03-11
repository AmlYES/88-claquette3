package com.example.controller;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/user")
 public class UserController {

    CartService cartService;
    UserService userService;
    ProductService productService;

    @Autowired
    public UserController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }
    //user dependent only
    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        try {
            return userService.addUser(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user data: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ArrayList<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        try {
            return userService.getUserById(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user ID: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("User not found: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        try {
            userService.getUserById(userId); // Ensure user exists before deleting
            userService.deleteUserById(userId);
            return "User deleted successfully";
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user ID: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException( e.getMessage());
        }
    }

    //user and order
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        try {
            return userService.getOrdersByUserId(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user ID: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("User not found: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        try {
            userService.addOrderToUser(userId);
            return "Order added successfully";
        } catch (IllegalArgumentException e) {
            return "Invalid user ID: " + e.getMessage();
        } catch (NoSuchElementException e) {
            return e.getMessage();
        } catch (IllegalStateException e) {
            return "Cannot place order: " + e.getMessage();
        }
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        try {
            userService.removeOrderFromUser(userId, orderId);
            return "Order removed successfully";
        } catch (IllegalArgumentException e) {
            return "Invalid input: " + e.getMessage();
        } catch (NoSuchElementException e) {
            return e.getMessage();
        }
    }

    //user and cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        try {
            userService.emptyCart(userId);
            return "Cart emptied successfully";
        } catch (IllegalArgumentException e) {
            return "Invalid user ID: " + e.getMessage();
        } catch (NoSuchElementException e) {
            return e.getMessage();
        }
    }

    //user, product and cart (mesh mawgodeen fe userservice)
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId){

        //assuming each user has one cart
        Cart cart = cartService.getCartByUserId(userId);

        if (cart == null) {
            cart = new Cart(userId);
            cartService.addCart(cart);
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return "Product with ID " + productId + " not found.";
        }

        cartService.addProductToCart(cart.getId(), product);
        cart = cartService.getCartByUserId(userId); //get the cart after adding the product
        return "Product added to cart";
    }


    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId){
        //assuming each user has one cart
        Cart cart = cartService.getCartByUserId(userId);

        if (cart == null || cart.getProducts().isEmpty()) {
            return "Cart is empty";
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return "Product with ID " + productId + " not found.";
        }

        StringBuilder response = new StringBuilder("\nCart before deleting the product:\n" + cart);
        cartService.deleteProductFromCart(cart.getId(), product);
        cart = cartService.getCartByUserId(userId); //get the cart after adding the product
        response.append("\nCart after deleting the product:\n" + cart);

        return "Product deleted from cart";

    }


}
