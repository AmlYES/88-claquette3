package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTests {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

//    // ------------------------ Tests for getUsers() -------------------------
//    //1
////    @Test
////    void getUsers_shouldReturnEmptyList() {
////        assertTrue(userService.getUsers().isEmpty(), "Users list  should be initially  empty");
////    }
//    //
//
//    // 2
//    @Test
//    void getUsers_shouldContainAddedUser() {
//        User user = new User("Bob");
//        userService.addUser(user);
//        ArrayList<User> users = userService.getUsers();
//        boolean exists = users.stream().anyMatch(u -> u.getId().equals(user.getId()));
//        assertTrue(exists, "User list should contain the added user");
//    }
//
//    //3
//    @Test
//    void getUsers_shouldReturnArrayList() {
//        // Act: Call the method
//        ArrayList<User> users = userService.getUsers();
//
//        // Assert: Ensure the returned object is an ArrayList
//        assertInstanceOf(ArrayList.class, users, "Expected getUsers() to return an ArrayList<User>");
//    }
//
//    //3
//    @Test
//    void getUsers_shouldReturnCorrectSizeAfterAddingUser() {
//        int initialSize = userService.getUsers().size();
//        User user = new User("Charlie");
//        userService.addUser(user);
//        int newSize = userService.getUsers().size();
//        assertEquals(initialSize + 1, newSize, "User list size should match expected count");
//    }
//
//    @Test
//    void getUsers_shouldReturnCorrectSizeAfterDeletingUser() {
//        // Arrange: Add a user first
//        User user = new User("Charlie");
//        userService.addUser(user);
//        int sizeAfterAdding = userService.getUsers().size();
//
//        // Act: Delete the user
//        userService.deleteUserById(user.getId());
//        int sizeAfterDeleting = userService.getUsers().size();
//
//        // Assert: The size should decrease by 1
//        assertEquals(sizeAfterAdding - 1, sizeAfterDeleting, "User list size should decrease by 1 after deletion");
//    }
//
//    // ------------------------  Tests for addUser(User user) -------------------------
//
//    // Tests for addUser(User user)
//    //add user 1
//    @Test
//    void addUser_withValidUser_shouldBeRetrievable() {
//        User user = new User("John Doe");
//        userService.addUser(user);
//        User retrievedUser = userService.getUserById(user.getId());
//        assertEquals(user.getId(), retrievedUser.getId(), "User ID should match");
//    }
//
//    //add user 2
//    @Test
//    void addUser_shouldAssignUniqueId() {
//        User user = new User("Jane Doe");
//        User returnedUser = userService.addUser(user);
//        assertNotNull(returnedUser.getId(), "User ID should not be null");
//    }
//
//    //add user 3
//    @Test
//    void addUser_shouldIncreaseUserListSize() {
//        int initialSize = userService.getUsers().size();
//        User user = new User("Alice");
//        userService.addUser(user);
//        int newSize = userService.getUsers().size();
//        assertEquals(initialSize + 1, newSize, "User list size should increase by 1");
//    }
//
//    //add user 4
//    @Test
//    void addUser_withNullUser_shouldThrowIllegalArgumentException() {
//        assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
//    }
//
//    @Test
//    void addUser_withExistingUser_shouldThrowIllegalStateException() {
//        UUID fixedId = UUID.randomUUID();
//        User user = new User(fixedId, "Ahmed", new ArrayList<Order>()); // Ensure the same ID is used
//        userService.addUser(user); // First time adding the user
//
//        // Attempting to add the same user again should throw an exception
//        assertThrows(IllegalStateException.class, () -> userService.addUser(user));
//    }
//
//
//    // ------------------------   Tests for getUserById(UUID userId) -------------------------
//
//
//    // Tests for getUserById(UUID userId)
//    @Test
//    void getUserById_withExistingUser_shouldNotReturnNull() {
//        User user = new User("Daniel");
//        userService.addUser(user);
//        User retrievedUser = userService.getUserById(user.getId());
//        assertNotNull(retrievedUser, "Existing user should not return null");
//    }
//
//    @Test
//    void getUserById_withNullId_shouldThrowIllegalArgumentException() {
//        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
//    }
//
//    @Test
//    void getUserById_withNonExistentId_shouldThrowNoSuchElementException() {
//        UUID nonExistentId = UUID.randomUUID();
//        assertThrows(NoSuchElementException.class, () -> userService.getUserById(nonExistentId));
//    }
//
//    @Test
//    void getUserById_shouldRetrieveCorrectUser() {
//        User user = new User("Eve");
//        userService.addUser(user);
//        User retrievedUser = userService.getUserById(user.getId());
//        assertEquals(user.getName(), retrievedUser.getName(), "User name should match");
//    }
//
//    // ------------------------ Tests for getOrdersByUserId(UUID userId) -------------------------


    // Tests for getOrdersByUserId(UUID userId)
    @Test
    void getOrdersByUserId_shouldReturnEmptyListForNewUser() {
        User user = new User("Frank");
        userService.addUser(user);
        List<Order> orders = userService.getOrdersByUserId(user.getId());
        assertTrue(orders.isEmpty(), "New user should have no orders");
    }

    @Test
    void getOrdersByUserId_shouldRetrieveCorrectOrders() {
        User user = new User("Grace");
        userService.addUser(user);
        Cart cart = cartService.getCartByUserId(user.getId());
        System.out.println(cart.toString());
        Product p= new Product("skincare",100);
        Product p1= new Product("skincahre",500);
        productService.addProduct(p);
         System.out.println(" cart products" +cart.getProducts());
        cartService.addProductToCart(cart.getId(),p);
        System.out.println(" cart products" +cart.getProducts());
        userService.addOrderToUser(user.getId());
        List<Order> orders = userService.getOrdersByUserId(user.getId());
        assertFalse(orders.isEmpty(), "Order should be added to user successfully");
    }

    @Test
    void getOrdersByUserId_withNullId_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.getOrdersByUserId(null),
                "Should throw IllegalArgumentException when userId is null");
    }

    @Test
    void getOrdersByUserId_withNonExistentUser_shouldThrowNoSuchElementException() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(NoSuchElementException.class, () -> userService.getOrdersByUserId(nonExistentId),
                "Should throw NoSuchElementException when user does not exist");
    }
    //    // ------------------------ Tests for addOrderToUser(UUID userId) -------------------------


    @Test
    void addOrderToUser_shouldIncreaseOrderCount() {
        User user = new User("Ahmed");
        userService.addUser(user);
        int initialOrders= userService.getOrdersByUserId(user.getId()).size();
        Cart cart = cartService.getCartByUserId(user.getId());
        Product p= new Product("Book",15);
        Product p1= new Product("Pen",5);
        productService.addProduct(p);
        cartService.addProductToCart(cart.getId(),p);
        userService.addOrderToUser(user.getId());
        int newOrders = userService.getOrdersByUserId(user.getId()).size();

        assertEquals(initialOrders + 1, newOrders, "Order count should increase by 1");
    }


    @Test
    void addOrderToUser_withNullUserId_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.addOrderToUser(null),
                "Should throw IllegalArgumentException when userId is null");
    }

    @Test
    void addOrderToUser_withNonExistentUser_shouldThrowNoSuchElementException() {
        UUID nonExistentUserId = UUID.randomUUID();
        assertThrows(NoSuchElementException.class, () -> userService.addOrderToUser(nonExistentUserId),
                "Should throw NoSuchElementException when user does not exist");
    }

    @Test
    void addOrderToUser_withNoCart_shouldThrowIllegalStateException() {
        // Arrange
        User user = new User("John Doe");
        userService.addUser(user);
        Cart cart = cartService.getCartByUserId(user.getId());
        cartService.deleteCartById(cart.getId());
        assertThrows(IllegalStateException.class, () -> userService.addOrderToUser(user.getId()),
                "Should throw IllegalStateException when no cart exists for the user");
    }

    @Test
    void addOrderToUser_withEmptyCart_shouldThrowIllegalStateException() {
        // Arrange
        User user = new User("Alice");
        userService.addUser(user);

        assertThrows(IllegalStateException.class, () -> userService.addOrderToUser(user.getId()),
                "Should throw IllegalStateException when cart is empty");
    }

    //    // ------------------------ Tests for emptyCart(UUID userId) -------------------------



    @Test
    void emptyCart_shouldRemoveAllProducts() {
        User user = new User("Ali");
        userService.addUser(user);
        Cart cart = cartService.getCartByUserId(user.getId());
        Product p= new Product("Book",15);
        Product p1= new Product("Pen",5);
        cartService.addProductToCart(cart.getId(),p);
        cartService.addProductToCart(cart.getId(),p1);
        userService.emptyCart(user.getId());
        assertTrue(cartService.getCartByUserId(user.getId()).getProducts().isEmpty(),
                "Cart should be empty after calling emptyCart");    }

    @Test
    void emptyCartTwice_shouldStillBeEmpty() {
        User user = new User("David");
        userService.addUser(user);
        userService.emptyCart(user.getId());
        userService.emptyCart(user.getId());
        assertTrue(userService.getOrdersByUserId(user.getId()).isEmpty(), "Cart should remain empty");
    }

    @Test
    void emptyCart_withNullUserId_shouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.emptyCart(null),
                "Should throw IllegalArgumentException when userId is null");
    }

    @Test
    void emptyCart_withNonExistentUser_shouldThrowNoSuchElementException() {
        // Arrange
        UUID fakeUserId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.emptyCart(fakeUserId),
                "Should throw NoSuchElementException when user does not exist");
    }

////    @Test
////    void removeOrderFromUser_shouldDecreaseOrderListSize() {
////        User user = new User("Eve");
////        userService.addUser(user);
////        userService.addOrderToUser(user.getId());
////        Order order = userService.getOrdersByUserId(user.getId()).get(0);
////        int initialSize = userService.getOrdersByUserId(user.getId()).size();
////        userService.removeOrderFromUser(user.getId(), order.getId());
////        int newSize = userService.getOrdersByUserId(user.getId()).size();
////        assertEquals(initialSize - 1, newSize, "Order list size should decrease by 1");
////    }
//
//    @Test
//    void removeNonExistentOrder_shouldThrowException() {
//        UUID userId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        assertThrows(RuntimeException.class, () -> userService.removeOrderFromUser(userId, orderId));
//    }
//
//    @Test
//    void removeOrderFromNonExistentUser_shouldThrowException() {
//        UUID nonExistentUserId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        assertThrows(RuntimeException.class, () -> userService.removeOrderFromUser(nonExistentUserId, orderId));
//    }
//
//    @Test
//    void deleteUserById_shouldRemoveUser() {
//        User user = new User("Frank");
//        userService.addUser(user);
//        userService.deleteUserById(user.getId());
//        assertThrows(RuntimeException.class, () -> userService.getUserById(user.getId()));
//    }
//
//    @Test
//    void deleteNonExistentUser_shouldThrowException() {
//        UUID nonExistentUserId = UUID.randomUUID();
//        assertThrows(RuntimeException.class, () -> userService.deleteUserById(nonExistentUserId));
//    }
//
////    @Test
////    void deleteUserWithOrders_shouldRemoveUser() {
////        User user = new User("George");
////        userService.addUser(user);
////
////        // Add a product to the user's cart before placing an order
////        Product product = new Product("Laptop", 1000);
////        cartService.addProductToCart(user.getId(), product);
////
////        userService.addOrderToUser(user.getId()); // Now it should work
////        userService.deleteUserById(user.getId());
////
////        assertThrows(RuntimeException.class, () -> userService.getUserById(user.getId()));
////    }
//
}