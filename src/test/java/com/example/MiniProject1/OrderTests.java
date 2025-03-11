//package com.example.MiniProject1;
//
//import com.example.model.Order;
//import com.example.service.OrderService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class OrderTests {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Test
//    void contextLoads() {
//    }
//
//    // Tests for addOrder(Order order)
//    @Test
//    void addOrder_withValidOrder_shouldBeRetrievable() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
//
//        // Act
//        orderService.addOrder(order);
//        Order retrievedOrder = orderService.getOrderById(order.getId());
//
//        // Assert
//        assertEquals(order.getId(), retrievedOrder.getId(), "Order ID should match");
//    }
//
//    @Test
//    void addOrder_shouldAssignUniqueId() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());
//
//        // Act
//        orderService.addOrder(order);
//
//        // Assert
//        assertNotNull(order.getId(), "Order ID should not be null");
//    }
//
//    @Test
//    void addOrder_shouldIncreaseOrderListSize() {
//        // Arrange
//        int initialSize = orderService.getOrders().size();
//        Order order = new Order(UUID.randomUUID(), 150.0, new ArrayList<>());
//
//        // Act
//        orderService.addOrder(order);
//        int newSize = orderService.getOrders().size();
//
//        // Assert
//        assertEquals(initialSize + 1, newSize, "Order list size should increase by 1");
//    }
//
//    // Tests for getOrders()
//    @Test
//    void getOrders_afterAddingOrder_shouldNotBeEmpty() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 120.0, new ArrayList<>());
//        orderService.addOrder(order);
//
//        // Act
//        ArrayList<Order> orders = orderService.getOrders();
//
//        // Assert
//        assertFalse(orders.isEmpty(), "Orders list should not be empty");
//    }
//
//    @Test
//    void getOrders_shouldReturnCorrectSize() {
//        // Arrange
//        int initialSize = orderService.getOrders().size();
//        Order order = new Order(UUID.randomUUID(), 300.0, new ArrayList<>());
//        orderService.addOrder(order);
//
//        // Act
//        int newSize = orderService.getOrders().size();
//
//        // Assert
//        assertEquals(initialSize + 1, newSize, "Order list size should increase by 1");
//    }
//
//    @Test
//    void getOrders_shouldContainAddedOrder() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 250.0, new ArrayList<>());
//        orderService.addOrder(order);
//
//        // Act
//        ArrayList<Order> orders = orderService.getOrders();
//
//        // Assert
//        boolean exists = orders.stream().anyMatch(o -> o.getId().equals(order.getId()));
//        assertTrue(exists, "Order list should contain the added order");
//    }
//
//
//    // Tests for getOrderById(UUID orderId)
//    @Test
//    void getOrderById_withValidId_shouldReturnCorrectOrder() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());
//        orderService.addOrder(order);
//
//        // Act
//        Order retrievedOrder = orderService.getOrderById(order.getId());
//
//        // Assert
//        assertEquals(order.getId(), retrievedOrder.getId(), "Retrieved order ID should match");
//    }
//
//    @Test
//    void getOrderById_withInvalidId_shouldReturnNull() {
//        // Act
//        Order retrievedOrder = orderService.getOrderById(UUID.randomUUID());
//
//        // Assert
//        assertNull(retrievedOrder, "Should return null for a non-existent order");
//    }
//
//    @Test
//    void getOrderById_withExistingOrder_shouldNotReturnNull() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 175.0, new ArrayList<>());
//        orderService.addOrder(order);
//
//        // Act
//        Order retrievedOrder = orderService.getOrderById(order.getId());
//
//        // Assert
//        assertNotNull(retrievedOrder, "Existing order should not return null");
//    }
//
//    // Tests for deleteOrderById(UUID orderId)
//    @Test
//    void deleteOrderById_withExistingOrder_shouldRemoveOrder() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 180.0, new ArrayList<>());
//        orderService.addOrder(order);
//
//        // Act
//        orderService.deleteOrderById(order.getId());
//
//        // Assert
//        assertNull(orderService.getOrderById(order.getId()), "Order should be removed after deletion");
//    }
//
//    @Test
//    void deleteOrderById_withNonExistingOrder_shouldThrowException() {
//        // Arrange
//        UUID randomId = UUID.randomUUID();
//
//        // Act & Assert
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            orderService.deleteOrderById(randomId);
//        });
//
//        assertEquals("Order with ID " + randomId + " not found.", exception.getMessage(), "Exception message should match");
//    }
//
//    @Test
//    void deleteOrderById_shouldDecreaseOrderListSize() {
//        // Arrange
//        Order order = new Order(UUID.randomUUID(), 220.0, new ArrayList<>());
//        orderService.addOrder(order);
//        int initialSize = orderService.getOrders().size();
//
//        // Act
//        orderService.deleteOrderById(order.getId());
//        int newSize = orderService.getOrders().size();
//
//        // Assert
//        assertEquals(initialSize - 1, newSize, "Order list size should decrease by 1 after deletion");
//    }
//}
