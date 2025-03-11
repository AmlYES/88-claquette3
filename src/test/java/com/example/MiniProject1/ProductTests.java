package com.example.MiniProject1;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.OrderService;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Executable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest

class ProductTests {
    @Autowired
    private ProductService productService;
    private ProductRepository productRepository;

    private Product product;

    @Test
    void contextLoads() {
    }

    @Test
    void addProduct_shouldAssignUniqueId() {
        Product product = new Product();

        productService.addProduct(product);

        assertNotNull(product.getId(), "Product ID should not be null");
    }

    @Test
    void addProduct_shouldIncreaseProductListSize() {
        int initialSize = productService.getProducts().size();
        Product product = new Product();

        productService.addProduct(product);
        int newSize = productService.getProducts().size();

        assertEquals(initialSize + 1, newSize, "Product list size should increase by 1");
    }

    @Test
    void addProduct_withValidProduct_shouldBeRetrievable() {
        // Arrange
        ProductRepository productRepository = new ProductRepository(); // Assuming real implementation

        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Test Product", 50.0);

        // Act
        productService.addProduct(product); // Add product
        Optional<Product> retrievedProduct = Optional.ofNullable(productRepository.getProductById(productId)); // Retrieve product

        // Assert
        assertTrue(retrievedProduct.isPresent(), "Product should be retrievable after adding");
        assertEquals(product.getId(), retrievedProduct.get().getId());
        assertEquals(product.getName(), retrievedProduct.get().getName());
        assertEquals(product.getPrice(), retrievedProduct.get().getPrice(), 0.01);
    }


    // Tests for getProducts
    @Test
    void getProducts_shouldReturnNonEmptyListAfterAddingProduct() {
        Product product = new Product();
        productService.addProduct(product);
        List<Product> products = productService.getProducts();
        assertFalse(products.isEmpty(), "Product list should not be empty");
    }

    @Test
    void getProducts_shouldContainAddedProduct() {
        Product product = new Product();
        productService.addProduct(product);

        List<Product> products = productService.getProducts();
        boolean exists = products.stream().anyMatch(p -> p.getId().equals(product.getId()));
        assertTrue(exists, "Product list should contain the added product");
    }

    @Test
    void getProducts_shouldReturnCorrectListSize() {
        int sizeBefore = productService.getProducts().size();
        productService.addProduct(new Product("Tablet", 500.0));
        assertEquals(sizeBefore + 1, productService.getProducts().size(), "Product list size should match");
    }

    @Test
    void getProducts_shouldReturnEmptyListInitially() {
        productService.getProducts()
                .forEach(product -> productService.deleteProductById(product.getId()));
        assertTrue(productService.getProducts().isEmpty(), "Product list should be empty initially");
    }

    @Test
    void getProducts_shouldFailIfRepositoryIsEmpty() {
        productService.getProducts()
                .forEach(product -> productService.deleteProductById(product.getId()));
        assertEquals(0, productService.getProducts().size(), "Fetching from empty repo should return size 0");
    }


    // Tests for getProductById
    @Test
    void getProductById_shouldReturnCorrectProduct() {
        Product product = new Product();
        productService.addProduct(product);
        UUID productId = product.getId();
        Product retrievedProduct = productService.getProductById(productId);
        assertEquals(productId, retrievedProduct.getId(), "Product ID should match");
    }

    @Test
    void getProductById_shouldReturnNullForNonExistentProduct() {
        Product retrievedProduct = productService.getProductById(UUID.randomUUID());
        assertNull(retrievedProduct, "Retrieving a non-existent product should return null");
    }

    @Test
    void getProductById_shouldFailForInvalidId() {
        assertNull(productService.getProductById(UUID.randomUUID()), "Should return null for non-existent ID");
    }


    // Tests for UpdateProduct
    @Test
    void updateProduct_shouldModifyProductDetails() {
        Product product = new Product("Headphones", 150.0);
        productService.addProduct(product);
        UUID productId = product.getId();
        productService.updateProduct(productId, "Wireless Headphones", 200.0);
        Product updatedProduct = productService.getProductById(productId);
        assertEquals("Wireless Headphones", updatedProduct.getName(), "Product name should be updated");
        assertEquals(200.0, updatedProduct.getPrice(), "Product price should be updated");
    }

    @Test
    void updateProduct_shouldHandleSamePrice() {
        Product product = new Product("Mouse Pad", 20.0);
        productService.addProduct(product);
        productService.updateProduct(product.getId(), "Mouse Pad", 20.0);
        assertEquals(20.0, productService.getProductById(product.getId()).getPrice(), "Price should remain same");
    }

    @Test
    void updateProduct_shouldFailForNonExistentProduct() {
        UUID nonExistentId = UUID.randomUUID(); // Generate a random UUID that doesn't exist
        assertThrows(IllegalArgumentException.class, () ->
                        productService.updateProduct(nonExistentId, "Ghost Product", 99.99),
                "Updating non-existent product should throw an exception");
    }

    // Test for applying a discount
    @Test
    void applyDiscount_shouldReduceProductPrices() {
        Product product1 = new Product("Keyboard", 100.0);
        Product product2 = new Product("Mouse", 50.0);
        productService.addProduct(product1);
        productService.addProduct(product2);

        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(product1.getId());
        productIds.add(product2.getId());

        productService.applyDiscount(20.0, productIds);

        Product discountedProduct1 = productService.getProductById(product1.getId());
        Product discountedProduct2 = productService.getProductById(product2.getId());

        assertEquals(80.0, discountedProduct1.getPrice(), "Keyboard price should be discounted by 20%");
        assertEquals(40.0, discountedProduct2.getPrice(), "Mouse price should be discounted by 20%");
    }

    @Test
    void applyDiscount_shouldFailForInvalidId() {
        UUID invalidId1 = UUID.randomUUID();
        UUID invalidId2 = UUID.randomUUID();
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(invalidId1);
        productIds.add(invalidId2);
        assertThrows(IllegalArgumentException.class, () ->
                        productService.applyDiscount(10.0 , productIds),
                " Discounting non-existent product should throw an exception");
    }

    @Test
    void applyDiscount_shouldNotChangePriceForZeroDiscount() {
        Product product = new Product("Monitor", 300.0);
        productService.addProduct(product);
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(product.getId());

        productService.applyDiscount(0.0, productIds);
        Product unchangedProduct = productService.getProductById(product.getId());

        assertEquals(300.0, unchangedProduct.getPrice(), "Applying a 0% discount should not change the price");
    }


    @Test
    void deleteProductById_shouldHandleLastProductDeletion() {
        productService.getProducts()
                .forEach(product -> productService.deleteProductById(product.getId()));

        Product product = new Product("Unique Product", 99.0);
        productService.addProduct(product);
        productService.deleteProductById(product.getId());
        assertTrue(productService.getProducts().isEmpty(), "List should be empty after deleting last product");
    }

    @Test
    void deleteProductById_shouldFailForNonExistentId() {
        assertThrows(IllegalArgumentException.class, () ->
                        productService.deleteProductById(UUID.randomUUID()),
                "Deleting non-existent product should throw exception");
    }

    @Test
    void deleteProductById_shoulddecreaseProductListSize() {
        int initialSize = productService.getProducts().size();

        ArrayList<Product> products = productService.getProducts();
        Product product = products.get(0);
        productService.deleteProductById(product.getId());
        int newSize = productService.getProducts().size();
        assertEquals(initialSize - 1, newSize, "Product list size should decrease by 1");
    }
}

