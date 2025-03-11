package com.example.MiniProject1;

import com.example.model.Order;
import com.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
    }

    // -------------- Tests for getOrders() --------------------------


    // 1
    @Test
    void getOrders_afterAddingOrder_shouldNotBeEmpty() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 120.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        ArrayList<Order> orders = orderService.getOrders();

        // Assert
        assertFalse(orders.isEmpty(), "Orders list should not be empty");
    }

    // 2
    @Test
    void getOrders_shouldReturnCorrectSize() {
        // Arrange
        int initialSize = orderService.getOrders().size();
        Order order = new Order(UUID.randomUUID(), 300.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        int newSize = orderService.getOrders().size();

        // Assert
        assertEquals(initialSize + 1, newSize, "Order list size should increase by 1");
    }

    // 3
    @Test
    void getOrders_shouldContainAddedOrder() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 250.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        ArrayList<Order> orders = orderService.getOrders();

        // Assert
        boolean exists = orders.stream().anyMatch(o -> o.getId().equals(order.getId()));
        assertTrue(exists, "Order list should contain the added order");
    }

    // 4
    @Test
    void getOrders_afterDeleting_shouldDecreaseListSize() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 220.0, new ArrayList<>());
        orderService.addOrder(order);
        int initialSize = orderService.getOrders().size();

        // Act
        orderService.deleteOrderById(order.getId());
        int newSize = orderService.getOrders().size();

        // Assert
        assertEquals(initialSize - 1, newSize, "Order list size should decrease by 1 after deletion");
    }


    // ------------------------- Tests for addOrder(Order order) ---------------------------------


    // 1 (valid case)
    @Test
    void addOrder_withValidOrder_shouldBeRetrievable() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());

        // Act
        orderService.addOrder(order);
        Order retrievedOrder = orderService.getOrderById(order.getId());

        // Assert
        assertEquals(order.getId(), retrievedOrder.getId(), "Order ID should match");
    }

    // 2 (valid case)
    @Test
    void addOrder_shouldAssignUniqueId() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());

        // Act
        orderService.addOrder(order);

        // Assert
        assertNotNull(order.getId(), "Order ID should not be null");
    }

    // 3 (valid case)
    @Test
    void addOrder_shouldIncreaseOrderListSize() {
        // Arrange
        int initialSize = orderService.getOrders().size();
        Order order = new Order(UUID.randomUUID(), 150.0, new ArrayList<>());

        // Act
        orderService.addOrder(order);
        int newSize = orderService.getOrders().size();

        // Assert
        assertEquals(initialSize + 1, newSize, "Order list size should increase by 1");
    }

    // 4 (invalid case: adding null order)
    @Test
    void addOrder_withNullOrder_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(null));
    }

    // 5 (invalid case: adding order for no user)
    @Test
    void addOrder_forNonExistingUser_shouldThrowException() {

        // Arrange
        Order order = new Order(null, 100.0, new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> orderService.addOrder(order));
    }


    // ------------------------- Tests for getOrderById(UUID orderId) -------------------------

    // 1 (valid case)
    @Test
    void getOrderById_withValidId_shouldReturnCorrectOrder() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        Order retrievedOrder = orderService.getOrderById(order.getId());

        // Assert
        assertEquals(order.getId(), retrievedOrder.getId(), "Retrieved order ID should match");
    }

    // 2 (valid case)
    @Test
    void getOrderById_withExistingOrder_shouldNotReturnNull() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 175.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        Order retrievedOrder = orderService.getOrderById(order.getId());

        // Assert
        assertNotNull(retrievedOrder, "Existing order should not return null");
    }


    // 3 (invalid case: invalid id)
    @Test
    void getOrderById_withInvalidId_shouldThrowException() {
//        // Act
//        Order retrievedOrder = orderService.getOrderById(UUID.randomUUID());
//
//        // Assert
//        assertNull(retrievedOrder, "Should return null for a non-existent order");

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> orderService.getOrderById(UUID.randomUUID()));
    }

    // 4 (invalid case: null id)
    @Test
    void getOrderById_withNullId_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(null));
    }


    // ------------------------- Tests for deleteOrderById(UUID orderId) -------------------------

    // 1 (valid case)
    @Test
    void deleteOrderById_withExistingOrder_shouldRemoveOrder() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), 180.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        orderService.deleteOrderById(order.getId());

        // Assert
        // we get the order again to check if it's deleted
        assertThrows(IllegalStateException.class, () -> orderService.getOrderById(order.getId()));
//        assertNull(orderService.getOrderById(order.getId()), "Order should be removed after deletion");
    }


    // 2 (invalid case: order not found)
    @Test
    void deleteOrderById_withNonExistingOrder_shouldThrowException() {
        // Arrange
        UUID randomId = UUID.randomUUID();

        // Act & Assert
//        Exception exception = assertThrows(IllegalStateException.class, () -> {
//            orderService.deleteOrderById(randomId);
//        });
//
//        assertEquals("Order with ID " + randomId + " not found.", exception.getMessage(), "Exception message should match");
        assertThrows(IllegalStateException.class, () -> orderService.deleteOrderById(randomId));
    }

    // 3 (invalid case: null id)
    @Test
    void deleteOrderById_withNullIdOrder_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.deleteOrderById(null));
    }


}
