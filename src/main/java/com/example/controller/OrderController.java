package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;



@RestController
@RequestMapping("/order")
public class OrderController {
    //The Dependency Injection Variables
    //The Constructor with the requried variables mapping the Dependency Injection.

    OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //order dependent only
    @PostMapping("/")
    public void addOrder(@RequestBody Order order){
        try{
            orderService.addOrder(order);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId){
        try{
            return orderService.getOrderById(orderId);

        }
        catch(Exception e){
            return null;
        }
    }

    @GetMapping("/")
    public ArrayList<Order> getOrders(){
        try{
            return orderService.getOrders();

        }
        catch(Exception e){
            return null;
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId){

        try{

//        Order order = orderService.getOrderById(orderId);
//        if(order == null){
//            return "Order not found";
//        }
        orderService.deleteOrderById(orderId);
        return "Order deleted successfully";
        }
        catch(Exception e){
            return e.getMessage();
        }
    }


}
