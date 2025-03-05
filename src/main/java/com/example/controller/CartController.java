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
        return cartService.addCart(cart);
    }

    @GetMapping("/")
    public ArrayList<Cart> getCarts(){
        return cartService.getCarts();
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId){
        return cartService.getCartById(cartId);
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId){
        Cart cart = cartService.getCartById(cartId);

        if(cart == null){
            return "Cart with ID " + cartId + " was not found";
        }

        cartService.deleteCartById(cartId);

        return "Cart deleted successfully";
    }

    //cart and product dependent
    @PutMapping("/addProduct/{cartId}")
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product){
        Cart cart = cartService.getCartById(cartId);

        if(cart == null){
            return "Cart with ID " + cartId + " was not found";
        }
        StringBuilder response = new StringBuilder("\nCart before adding the product:\n" + cart);
        cartService.addProductToCart(cartId,product);
        cart = cartService.getCartById(cartId);
        response.append("\nCart after adding the product:\n" + cart);

        return "Product: " + product + " was added successfully \n" + response;
    }

}
