package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.service.CartService;
import com.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class CartTests {
    @Autowired
    private CartService cartService;

    @Test
    void contextLoads() {
    }

    // Tests for addCart
    @Test
    void addCart_withValidCart_shouldBeRetrievable() {

        Cart cart = new Cart(UUID.randomUUID());

        // Act
        cartService.addCart(cart);
        Cart retrievedCart = cartService.getCartById(cart.getId());

        // Assert
        assertEquals(cart.getId(), retrievedCart.getId(), "Cart ID should match");
    }

    @Test
    void addCart_shouldAssignUniqueId() {
        // Arrange
       Cart cart= new Cart(UUID.randomUUID());

        // Act
        cartService.addCart(cart);

        // Assert
        assertNotNull(cart.getId(), "Cart ID should not be null");
    }


    @Test
    void addCart_shouldIncreaseCartListSize() {
        // Arrange
        int length = cartService.getCarts().size();
        Cart cart = new Cart(UUID.randomUUID());

        // Act
        cartService.addCart(cart);
        int newSize = cartService.getCarts().size();

        // Assert
        assertEquals(length + 1, newSize, "Cart list size should increase by 1");
    }

    // Tests for getOrders()
    @Test
    void getCarts_afterAddingCart_shouldNotBeEmpty(){
        // Arrange
        Cart cart= new Cart(UUID.randomUUID());

        // Act
        cartService.addCart(cart);
        boolean cartsEmpty= false;
        List<Cart> carts = cartService.getCarts();

        //Assert
       assertEquals(cartsEmpty,carts.isEmpty(),"Cart list should not be empty");
    }

    @Test
    void getCarts_shouldReturnCorrectSize() {
        // Arrange
        int initialSize = cartService.getCarts().size();
        Cart cart = new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        int newSize = cartService.getCarts().size();

        // Assert
        assertEquals(initialSize + 1, newSize, "Carts list size should increase by 1");
    }

    @Test
    void getCarts_shouldContainAddedCart() {
        // Arrange
       Cart cart= new Cart(UUID.randomUUID());
       cartService.addCart(cart);

        // Act
        ArrayList<Cart> carts = cartService.getCarts();

        // Assert
        boolean exists = carts.stream().anyMatch(c -> c.getId().equals(cart.getId()));
        assertTrue(exists, "Cart list should contain the added order");
    }

    @Test
    void getCart_shouldRetrieveCorrectCartById(){

        // Arrange
        Cart cart= new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        UUID cartID= cart.getId();
        Cart retrievedCart = cartService.getCartById(cartID);

        // Assert
        assertEquals(cartID,retrievedCart.getId(),"Cart ID should match");
    }

    @Test
    void getUserCart_shouldRetrieveCorrectCartID(){

        // Arrange
        Cart cart= new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        UUID userID= cart.getUserId();
        UUID cartID= cart.getId();
        Cart retrievedCart= cartService.getCartByUserId(userID);

        //Arrange
        assertEquals(cartID,retrievedCart.getId(),"Cart ID should match");
    }

    @Test
    void getUserCart_shouldRetrieveCorrectCartUserID(){

        // Arrange
        Cart cart= new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        UUID userID= cart.getUserId();
        UUID cartID= cart.getId();
        Cart retrievedCart= cartService.getCartByUserId(userID);

        //Arrange
        assertEquals(userID,retrievedCart.getUserId(),"Cart UserID should match");
    }

    @Test
    void getUserCart_shouldRetrieveCorrectCartProducts(){

        // Arrange
        Cart cart= new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        UUID userID= cart.getUserId();
        Cart retrievedCart= cartService.getCartByUserId(userID);

        //Arrange
        assertEquals(cart.getProducts(),retrievedCart.getProducts(),"Cart products should match");
    }

    @Test
    void addProductToCart_shouldFindProductAddedToCart(){

        // Arrange
        Product product=new Product("pen",10.0);
        Cart cart= new Cart(UUID.randomUUID());
        UUID cartId=cart.getId();
        cartService.addCart(cart);
        cartService.addProductToCart(cartId,product);

        // Act
        cartService.addProductToCart(cartId,product);
        List<Cart> carts= cartService.getCarts();
        Cart retrievedCart= cartService.getCartById(cartId);
        List<Product> productsRetrieved= retrievedCart.getProducts();

                //Arrange
        assertTrue(productsRetrieved.stream().anyMatch(p ->p.getPrice()==product.getPrice() &&p.getName().equals(product.getName()) && p.getId().equals(product.getId())));
    }

    @Test
    void addProductToCart_shouldIncrementProductsByOne(){

        // Arrange
        Product product=new Product("pen",10.0);
        Cart cart= new Cart(UUID.randomUUID());
        UUID cartId=cart.getId();
        int size= cart.getProducts().size();
        cartService.addCart(cart);
        cartService.addProductToCart(cartId,product);

        // Act
        cartService.addProductToCart(cartId,product);
        List<Cart> carts= cartService.getCarts();
        Cart retrievedCart= cartService.getCartById(cartId);
        List<Product> productsRetrieved= retrievedCart.getProducts();
        int sizeRetrieved= productsRetrieved.size();

        // Assert
        assertEquals(size+1,sizeRetrieved,"Products list size should be incremented by one.");
    }








}
