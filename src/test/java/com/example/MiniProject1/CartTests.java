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

    //------------------------------ Tests for getCarts() ------------------------------

    // 1
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

    // 2
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

    // 3
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

    // 4
    @Test
    void getCarts_afterDeleting_shouldDecreaseListSize() {
        // Arrange
        Cart cart = new Cart(UUID.randomUUID());
        cartService.addCart(cart);
        int initialSize = cartService.getCarts().size();

        // Act
        cartService.deleteCartById(cart.getId());
        int newSize = cartService.getCarts().size();

        // Assert
        assertEquals(initialSize - 1, newSize, "Cart list size should decrease by 1 after deletion");
    }

    // ------------------------------ Tests for addCart() ------------------------------

    // 1 (valid case)
    @Test
    void addCart_withValidCart_shouldBeRetrievable() {

        Cart cart = new Cart(UUID.randomUUID());

        // Act
        cartService.addCart(cart);
        Cart retrievedCart = cartService.getCartById(cart.getId());

        // Assert
        assertEquals(cart.getId(), retrievedCart.getId(), "Cart ID should match");
    }

    // 2 (valid case)
    @Test
    void addCart_shouldAssignUniqueId() {
        // Arrange
       Cart cart= new Cart(UUID.randomUUID());

        // Act
        cartService.addCart(cart);

        // Assert
        assertNotNull(cart.getId(), "Cart ID should not be null");
    }

    // 3 (valid case)
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

    // 4 (invalid case: cart null)
    @Test
    void addCart_withNullCart_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addCart(null));
    }

    // 5 (invalid case: user already have a cart)
    @Test
    void addCart_userAlreadyHaveCart_shouldThrowException(){
        // Arrange: Create a user and assign a cart to them
        UUID userId = UUID.randomUUID();
        Cart firstCart = new Cart(UUID.randomUUID());
        firstCart.setUserId(userId);
        cartService.addCart(firstCart);

        // Act & Assert: Adding another cart for the same user should throw an exception
        Cart secondCart = new Cart(UUID.randomUUID());
        secondCart.setUserId(userId);
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addCart(secondCart);
        });
    }


    // ------------------------------ Test for getCartById() ------------------------------

    // 1 (valid case)
    @Test
    void getCartByID_shouldRetrieveCorrectCartById(){

        // Arrange
        Cart cart= new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        UUID cartID= cart.getId();
        Cart retrievedCart = cartService.getCartById(cartID);

        // Assert
        assertEquals(cartID,retrievedCart.getId(),"Cart ID should match");
    }

    // 2 (valid case)
    @Test
    void getCartByID_shouldHaveCorrectUserId(){

        // Arrange
        UUID userID= UUID.randomUUID();
        Cart cart= new Cart(userID);
        cartService.addCart(cart);

        // Act
        UUID cartID= cart.getId();
        Cart retrievedCart = cartService.getCartById(cartID);

        // Assert
        assertEquals(userID,retrievedCart.getUserId(),"Cart fetched by ID should have matching userID");
    }

    // 3 (valid case)
    @Test
    void getCartByID_shouldReturnNullForInvalidCartId() {
        // Arrange (random cart that doesn't exist)
        UUID invalidCartID = UUID.randomUUID();


        // Act & Assert
        assertThrows(IllegalStateException.class, () -> cartService.getCartById(invalidCartID));

    }

    // 4 (invalid case: null cartId)
    @Test
    void getCartByID_withNullCartId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.getCartById(null));
    }

    // ------------------------------ Test for getCartByUserId() ------------------------------

    // 1 (valid case)
    @Test
    void getUserCart_shouldRetrieveCorrectCartID(){
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart expectedCart = new Cart(userId);
        cartService.addCart(expectedCart);

        // Act
        Cart retrievedCart = cartService.getCartByUserId(userId);

        // Assert
        assertEquals(expectedCart.getId(), retrievedCart.getId(), "Cart ID should match");
    }

    // 2 (valid case)
    @Test
    void getUserCart_shouldRetrieveNonNullCart(){
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart expectedCart = new Cart(userId);
        cartService.addCart(expectedCart);

        // Act
        Cart retrievedCart = cartService.getCartByUserId(userId);

        // Assert
        assertNotNull(retrievedCart, "Retrieved cart should not be null");
    }

    // 3 (valid case)
    @Test
    void getUserCart_shouldRetrieveCorrectCartProductsSize(){
        // Arrange
        UUID userId = UUID.randomUUID();
        List<Product> products = List.of(new Product("Item1", 10), new Product("Item2", 20));
        Cart expectedCart = new Cart(UUID.randomUUID(), userId, products);
        cartService.addCart(expectedCart);

        // Act
        Cart retrievedCart = cartService.getCartByUserId(userId);

        // Assert
        assertEquals(products.size(), retrievedCart.getProducts().size(), "Cart products size should match");
    }

    // 4 (invalid case: null userId)
    @Test
    void getUserCart_forNullUserId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.getCartByUserId(null));
    }

    // ------------------------------ Test for addProductToCart() ------------------------------

    //1 (valid case)
    @Test
    void addProductToCart_shouldFindProductAddedToCart(){

        // Arrange
        Cart cart = new Cart(UUID.randomUUID());
        UUID cartId = cart.getId();
        cartService.addCart(cart);

        Product product=new Product("pen",10.0);

        // Act
        cartService.addProductToCart(cartId,product);
        Cart retrievedCart = cartService.getCartById(cartId);
        List<Product> productsRetrieved= retrievedCart.getProducts();

        // Assert
        assertTrue(productsRetrieved.stream().anyMatch(p ->p.getPrice()==product.getPrice() &&p.getName().equals(product.getName()) && p.getId().equals(product.getId())));
    }

    //2 (valid case)
    @Test
    void addProductToCart_shouldIncrementProductsByOne(){

        // Arrange
        Cart cart = new Cart(UUID.randomUUID());
        UUID cartId = cart.getId();
        cartService.addCart(cart);

        Product product=new Product("pen",10.0);

        // Act
        cartService.addProductToCart(cartId,product);
        int size = cart.getProducts().size();
        Cart retrievedCart = cartService.getCartById(cartId);
        List<Product> productsRetrieved= retrievedCart.getProducts();

        int sizeRetrieved= productsRetrieved.size();

        // Assert
        assertEquals(size + 1,sizeRetrieved,"Products list size should be incremented by one.");
    }

    //3 (invalid case: null product)
    @Test
    void addProductToCart_withNullProduct_shouldThrowException(){
        Cart cart = new Cart(UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> cartService.addProductToCart(cart.getId(), null));
    }

    //4 (invalid case: null cartId)
    @Test
    void addProductToCart_withNullProductId_shouldThrowException(){
        Product product = new Product("pen",10.0);
        assertThrows(IllegalArgumentException.class, () -> cartService.addProductToCart(null, product));
    }

    // ------------------------------ Test for deleteProductFromCart() ------------------------------

    //1 (valid case)
    @Test
    void deleteProductFromCart_shouldRemoveProductSuccessfully() {
        // Arrange
        Cart cart = new Cart(UUID.randomUUID());
        Product product = new Product("Laptop", 1200.0);
        cartService.addCart(cart);
        cartService.addProductToCart(cart.getId(), product);
        System.out.println(cartService.getCartById(cart.getId()).getProducts());

        // Act
        cartService.deleteProductFromCart(cart.getId(), product);

        // Assert
        assertTrue(cart.getProducts().isEmpty());
    }


    //2 (invalid case: product not found)
//    @Test
//    void deleteProductFromCart_whenProductNotInCart_shouldThrowException() {
//        // Arrange
//        Cart cart = new Cart(UUID.randomUUID());
//        Product product = new Product("Laptop", 1200.0);
//        cartService.addCart(cart);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class,
//                () -> cartService.deleteProductFromCart(cart.getId(), product));
//
//    }

    //3 (invalid case: cart not found)
    @Test
    void deleteProductFromCart_whenCartDoesNotExist_shouldThrowException() {
        // Act & Assert
        UUID nonExistentCartId = UUID.randomUUID();
        Product product = new Product("Laptop", 1200.0);

        assertThrows(IllegalStateException.class,
                () -> cartService.deleteProductFromCart(nonExistentCartId, product));

    }

    //4 (invalid case: cartId null)
    @Test
    void deleteProductFromCart_withNullCartId_shouldThrowException() {
        Product product = new Product("Laptop", 1200.0);
        assertThrows(IllegalArgumentException.class, () -> cartService.deleteProductFromCart(null, product));
    }


    // ------------------------------ Test for deleteCartById() ------------------------------

    //1 (invalid case: cartId null)
    @Test
    void deleteCart_withNullCartId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.deleteCartById(null));
    }

    //2 (invalid case: cart not found)
    @Test
    void deleteCart_whenCartDoesNotExist_shouldThrowException() {
        // Act & Assert
        UUID nonExistentCartId = UUID.randomUUID();

        assertThrows(IllegalStateException.class,
                () -> cartService.deleteCartById(nonExistentCartId));

    }


    // 3 (valid case)
    @Test
    void deleteCartById_shouldDeleteCartSuccessfully() {
        // Arrange
        Cart cart = new Cart(UUID.randomUUID());
        cartService.addCart(cart);

        // Act
        cartService.deleteCartById(cart.getId());

        // Assert
        assertThrows(IllegalStateException.class, () -> cartService.deleteCartById(cart.getId()));
    }


    // ------------------------------ Test for emptyCart() ------------------------------

    //1 (invalid case: null userId)
    @Test
    void emptyCart_withNullUserId_shouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> cartService.emptyCart(null));
    }

    //2 (invalid case: cart does not exist)
    @Test
    void emptyCart_doesNotExist_shouldThrowException(){
        assertThrows(IllegalStateException.class, () -> cartService.emptyCart(UUID.randomUUID()));
    }

    //3 (valid case)
    @Test
    void emptyCart_shouldRemoveAllProducts() {
        // Arrange
        Cart cart = new Cart(UUID.randomUUID());
        cartService.addCart(cart);
        cartService.addProductToCart(cart.getId(), new Product("Phone", 800.0));

        // Act
        cartService.emptyCart(cart.getUserId());

        // Assert
        assertTrue(cartService.getCartById(cart.getId()).getProducts().isEmpty(),
                "Cart should be empty after calling emptyCart");
    }







}
