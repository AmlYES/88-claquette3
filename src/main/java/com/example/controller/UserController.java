package com.example.controller;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserController {


    //user dependent only
    @PostMapping("/")
    public User addUser(@RequestBody User user){
        return user;
    }

    @GetMapping("/")
    public ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<User>();
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        User user = new User();
        return user;
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId){
        return "";
    }

    //user and order
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId){
        List<Order> orders = new ArrayList<Order>();
        return orders;

    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId){
        return "";
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId){

        return "";
    }

    //user and cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId){

        return "";
    }

    //user, product and cart
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId){

        return "";
    }


    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId){
        return "";
    }


}
