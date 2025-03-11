package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart>{
    @Autowired
    CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        super();
    }

    public Cart addCart(Cart cart){
        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts(){
        return cartRepository.getCarts();
    }
    public Cart getCartById(UUID cartId){
        return cartRepository.getCartById(cartId);
    }
    public Cart getCartByUserId(UUID userId){
        return cartRepository.getCartByUserId(userId);
    }
    public void addProductToCart(UUID cartId, Product product){
         cartRepository.addProductToCart(cartId,product);
        System.out.println(" cart products" + cartRepository.getCartById(cartId).toString());

    }
    public void deleteProductFromCart(UUID cartId, Product product){
         cartRepository.deleteProductFromCart(cartId,product);
    }
    public void deleteCartById(UUID cartId){
         cartRepository.deleteCartById(cartId);
    }

    public void emptyCart(UUID userId) {
        cartRepository.emptyCart(userId);
    }
//The Dependency Injection Variables
//The Constructor with the required variables mapping the Dependency Injection.
}
// add cart 3
// get carts 3
//get by id 1
// get by user id 3
// add product 2