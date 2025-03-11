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
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping("/")
    public ArrayList<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
        public User getUserById(@PathVariable UUID userId){
        return userService.getUserById(userId);
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId){

        try{
            User user = userService.getUserById(userId); // Fetch deleted user

            if (user == null) {
                return "User not found";
            }

            userService.deleteUserById(userId);

            return "User deleted successfully";

        }
        catch (NoSuchElementException e){
            return "User not found";
        }
    }

    //user and order
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId){

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found.");
        }
        return userService.getOrdersByUserId(userId);
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId){


        // CATCHHH

        User user = userService.getUserById(userId); // Fetch the user
        if (user == null) {
            return "User with ID " + userId + " not found.";
        }
        userService.addOrderToUser(userId);
        return "Order added successfully";
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId){

        User user = userService.getUserById(userId);
        if (user == null) {
            return "User with ID " + userId + " not found.";
        }
        List<Order> orders = userService.getOrdersByUserId(userId);
        // Check if the order exists
        boolean orderExists = orders.stream().anyMatch(order -> order.getId().equals(orderId));

        if (!orderExists) {
            return "Order with ID " + orderId + " not found for user " + userId;
        }

        userService.removeOrderFromUser(userId, orderId);
        return "Order removed successfully";
    }

    //user and cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId){
        User user = userService.getUserById(userId);
        if (user == null) {
            return "User not found";
        }
        userService.emptyCart(userId);
        return "Cart emptied successfully";
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
