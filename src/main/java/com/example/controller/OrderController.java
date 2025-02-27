package com.example.controller;

import com.example.model.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    //The Dependency Injection Variables
    //The Constructor with the requried variables mapping the Dependency Injection.


    //order only dependent
    @PostMapping("/")
    public void addOrder(@RequestBody Order order){

    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId){
        return null;
    }

    @GetMapping("/")
    public ArrayList<Order> getOrders(){
        return null;
    }

    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId){
        return  "";
    }


}
