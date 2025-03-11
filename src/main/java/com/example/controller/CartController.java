package com.example.controller;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {
    //The Dependency Injection Variables
    //The Constructor with the requried variables mapping the Dependency Injection.

    CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //cart dependent only
    @PostMapping("/")
    public Cart addCart(@RequestBody Cart cart){
        try{
            return cartService.addCart(cart);
        }
        catch(Exception e){
            return null;
        }
    }

    @GetMapping("/")
    public ArrayList<Cart> getCarts(){

        return cartService.getCarts();
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId){
        try{
            return cartService.getCartById(cartId);
        }
        catch(Exception e){
            return null;
        }
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId){
        try {
//            Cart cart = cartService.getCartById(cartId);
//
//            if(cart == null){
//                return "Cart with ID " + cartId + " was not found";
//            }

            cartService.deleteCartById(cartId);

            return "Cart deleted successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    //cart and product dependent
    @PutMapping("/addProduct/{cartId}")
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product){

        try{
//        Cart cart = cartService.getCartById(cartId);
//
//        if(cart == null){
//            return "Cart with ID " + cartId + " was not found";
//        }
//        StringBuilder response = new StringBuilder("\nCart before adding the product:\n" + cart);
            cartService.addProductToCart(cartId,product);
//        cart = cartService.getCartById(cartId);
//        response.append("\nCart after adding the product:\n" + cart);

        return "Product: " + product + " was added successfully";
        }
        catch(Exception e){
            return e.getMessage();
        }
    }

}
