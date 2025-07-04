package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart>{
    @Autowired
    CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public CartService(CartRepository cartRepository) {
        super();
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        if(getCartByUserId(cart.getUserId()) != null) {
            throw new IllegalArgumentException("User already have a cart");
        }
        return cartRepository.addCart(cart);
    }
    public ArrayList<Cart> getCarts(){
        return cartRepository.getCarts();
    }
    public Cart getCartById(UUID cartId){
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        Cart cart = cartRepository.getCartById(cartId);
        if (cart == null) {
            throw new IllegalStateException("Cart not found with ID: " + cartId);
        }
        return cart;
    }
    public Cart getCartByUserId(UUID userId){
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return cartRepository.getCartByUserId(userId);
    }
    public void addProductToCart(UUID cartId, Product product){
        if (cartId == null || product == null) {
            throw new IllegalArgumentException("Cart ID and Product cannot be null");
        }
        Cart cart = getCartById(cartId);
        if (cart == null) {
            throw new IllegalStateException("Cart not found with ID: " + cartId);
        }

         cartRepository.addProductToCart(cartId,product);
    }
    public void deleteProductFromCart(UUID cartId, Product product) {
        if (cartId == null || product == null) {
            throw new IllegalArgumentException("Cart ID and Product cannot be null");
        }
        Cart cart = getCartById(cartId);
        if (cart == null) {
            throw new IllegalStateException("Cart not found with ID: " + cartId);
        }
//        if (!cart.getProducts().contains(productRepository.getProductById(product.getId()))) {
//            throw new IllegalStateException("Product not found in cart");
//        }
        cartRepository.deleteProductFromCart(cartId, product);
    }
    public void deleteCartById(UUID cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        Cart cart = getCartById(cartId);
        if (cart == null) {
            throw new IllegalStateException("Cannot delete. Cart not found with ID: " + cartId);
        }
        cartRepository.deleteCartById(cartId);
    }

    public void emptyCart(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            throw new IllegalStateException("Cart does not exist");
        }
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