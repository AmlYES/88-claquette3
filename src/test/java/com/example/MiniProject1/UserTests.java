//package com.example.MiniProject1;
//
//import com.example.model.Order;
//import com.example.model.Product;
//import com.example.model.User;
//import com.example.repository.CartRepository;
//import com.example.service.CartService;
//import com.example.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class UserTests {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private CartService cartService;
//
//    @Test
//    void contextLoads() {
//    }
//
//    // Tests for addUser(User user)
//    @Test
//    void addUser_withValidUser_shouldBeRetrievable() {
//        User user = new User("John Doe");
//        userService.addUser(user);
//        User retrievedUser = userService.getUserById(user.getId());
//        assertEquals(user.getId(), retrievedUser.getId(), "User ID should match");
//    }
//
//    @Test
//    void addUser_shouldAssignUniqueId() {
//        User user = new User("Jane Doe");
//        userService.addUser(user);
//        assertNotNull(user.getId(), "User ID should not be null");
//    }
//
//    @Test
//    void addUser_shouldIncreaseUserListSize() {
//        int initialSize = userService.getUsers().size();
//        User user = new User("Alice");
//        userService.addUser(user);
//        int newSize = userService.getUsers().size();
//        assertEquals(initialSize + 1, newSize, "User list size should increase by 1");
//    }
//
//    // Tests for getUsers()
//    @Test
//    void getUsers_shouldContainAddedUser() {
//        User user = new User("Bob");
//        userService.addUser(user);
//        ArrayList<User> users = userService.getUsers();
//        boolean exists = users.stream().anyMatch(u -> u.getId().equals(user.getId()));
//        assertTrue(exists, "User list should contain the added user");
//    }
//
//    @Test
//    void getUsers_shouldReturnNonEmptyList() {
//        assertFalse(userService.getUsers().isEmpty(), "User list should not be empty");
//    }
//
//    @Test
//    void getUsers_shouldReturnCorrectSizeAfterAddingUser() {
//        int initialSize = userService.getUsers().size();
//        User user = new User("Charlie");
//        userService.addUser(user);
//        int newSize = userService.getUsers().size();
//        assertEquals(initialSize + 1, newSize, "User list size should match expected count");
//    }
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
//    void getUserById_withNonExistingUser_shouldThrowException() {
//        UUID nonExistentUserId = UUID.randomUUID();
//        assertThrows(RuntimeException.class, () -> userService.getUserById(nonExistentUserId), "Should throw exception for non-existing user");
//    }
//
//
//    @Test
//    void getUserById_shouldRetrieveCorrectUser() {
//        User user = new User("Eve");
//        userService.addUser(user);
//        User retrievedUser = userService.getUserById(user.getId());
//        assertEquals(user.getName(), retrievedUser.getName(), "User name should match");
//    }
//
//    // Tests for getOrdersByUserId(UUID userId)
//    @Test
//    void getOrdersByUserId_shouldReturnEmptyListForNewUser() {
//        User user = new User("Frank");
//        userService.addUser(user);
//        List<Order> orders = userService.getOrdersByUserId(user.getId());
//        assertTrue(orders.isEmpty(), "New user should have no orders");
//    }
//
////    @Test
////    void getOrdersByUserId_shouldRetrieveCorrectOrders() {
////        User user = new User("Grace");
////        userService.addUser(user);
////        userService.addOrderToUser(user.getId());
////        List<Order> orders = userService.getOrdersByUserId(user.getId());
////        assertFalse(orders.isEmpty(), "User should have at least one order");
////    }
//
//    @Test
//    void getOrdersByUserId_withNonExistingUser_shouldReturnEmptyList() {
//        List<Order> orders = userService.getOrdersByUserId(UUID.randomUUID());
//        assertTrue(orders.isEmpty(), "Non-existing user should return empty order list");
//    }
//
////    @Test
////    void addOrderToUser_shouldIncreaseOrderCount() {
////        User user = new User("Alice");
////        userService.addUser(user);
////
////        // 🛒 Add a product to the cart BEFORE placing an order
////        Product product = new Product("Laptop", 1200.00);
////        cartService.addProductToCart(user.getId(), product);   // Ensure this method exists
////
////        int initialOrders = userService.getOrdersByUserId(user.getId()).size();
////        userService.addOrderToUser(user.getId());
////        int newOrders = userService.getOrdersByUserId(user.getId()).size();
////
////        assertEquals(initialOrders + 1, newOrders, "Order count should increase by 1");
////    }
//
//
//    @Test
//    void addOrderToNonExistentUser_shouldThrowException() {
//        UUID nonExistentUserId = UUID.randomUUID();
//        assertThrows(RuntimeException.class, () -> userService.addOrderToUser(nonExistentUserId));
//    }
//
////    @Test
////    void addMultipleOrdersToUser_shouldIncreaseOrderCount() {
////        User user = new User("Bob");
////        userService.addUser(user);
////
////        cartService.addProductToCart(user.getId(), new Product("Item1", 10.0));
////        cartService.addProductToCart(user.getId(), new Product("Item2", 15.0));
////
////        userService.addOrderToUser(user.getId());
////        userService.addOrderToUser(user.getId());
////        assertEquals(2, userService.getOrdersByUserId(user.getId()).size(), "User should have 2 orders");
////    }
//
//
//    @Test
//    void emptyCart_shouldRemoveAllProducts() {
//        User user = new User("Charlie");
//        userService.addUser(user);
//        userService.emptyCart(user.getId());
//        assertTrue(userService.getOrdersByUserId(user.getId()).isEmpty(), "Cart should be empty");
//    }
//
//    @Test
//    void emptyCartTwice_shouldStillBeEmpty() {
//        User user = new User("David");
//        userService.addUser(user);
//        userService.emptyCart(user.getId());
//        userService.emptyCart(user.getId());
//        assertTrue(userService.getOrdersByUserId(user.getId()).isEmpty(), "Cart should remain empty");
//    }
//
//    @Test
//    void emptyCartForNonExistentUser_shouldThrowException() {
//        UUID nonExistentUserId = UUID.randomUUID();
//        assertThrows(RuntimeException.class, () -> userService.emptyCart(nonExistentUserId));
//    }
//
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
//}