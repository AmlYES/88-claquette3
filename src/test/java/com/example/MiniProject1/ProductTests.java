package com.example.MiniProject1;

import com.example.model.Product;
import com.example.service.OrderService;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTests {
    @Autowired
    private ProductService productService;

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
    void addProduct_shouldFailOnNullName() {
        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(new Product(null, 100.0)),
                "Adding product with null name should throw exception");
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
    void getProductById_shouldRetrieveLastAddedProduct() {
        Product product1 = new Product();
        Product product2 = new Product();
        productService.addProduct(product1);
        productService.addProduct(product2);
        assertEquals(product2, productService.getProductById(product2.getId()), "Should retrieve last added product");
    }

    @Test
    void getProductById_shouldFailForInvalidId() {
        assertNull(productService.getProductById(UUID.randomUUID()), "Should return null for non-existent ID");
    }

    // Tests for deleteProduct
    @Test
    void deleteProduct_shouldRemoveProductFromList() {
        Product product = new Product();
        productService.addProduct(product);
        UUID productId = product.getId();
        productService.deleteProductById(productId);
        Product retrievedProduct = productService.getProductById(productId);
        assertNull(retrievedProduct, "Deleted product should not be retrievable");
    }
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
        assertThrows(IllegalArgumentException.class, () ->
                        productService.updateProduct(UUID.randomUUID(), "Ghost Product", 99.99),
                "Updating non-existent product should throw exception");
    }

    // **Test for applying a discount**
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
    void applyDiscount_shouldReducePricesCorrectly() {
        Product p1 = new Product("Monitor", 200.0);
        Product p2 = new Product("Chair", 400.0);
        productService.addProduct(p1);
        productService.addProduct(p2);
        productService.applyDiscount(20.0, (ArrayList<UUID>) List.of(p1.getId(), p2.getId()));
        assertEquals(160.0, productService.getProductById(p1.getId()).getPrice(), "Monitor should be discounted");
        assertEquals(320.0, productService.getProductById(p2.getId()).getPrice(), "Chair should be discounted");
    }

    @Test
    void applyDiscount_shouldAllow100PercentDiscount() {
        Product product = new Product("Gift Card", 50.0);
        productService.addProduct(product);
        productService.applyDiscount(100.0, (ArrayList<UUID>) List.of(product.getId()));
        assertEquals(0.0, productService.getProductById(product.getId()).getPrice(), "Price should be zero after 100% discount");
    }

    @Test
    void applyDiscount_shouldFailForInvalidId() {
        assertThrows(IllegalArgumentException.class, () ->
                        productService.applyDiscount(10.0, (ArrayList<UUID>) List.of(UUID.randomUUID())),
                "Applying discount to non-existent product should throw exception");
    }


    @Test
    void deleteProduct_shouldDecreaseProductListSize() {
        Product product = new Product();
        productService.addProduct(product);
        int initialSize = productService.getProducts().size();
        productService.deleteProductById(product.getId());
        int newSize = productService.getProducts().size();
        assertEquals(initialSize - 1, newSize, "Product list size should decrease by 1");
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

}
