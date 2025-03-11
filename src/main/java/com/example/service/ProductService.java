package com.example.service;

import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.service.MainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {

    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        super();
        this.productRepository = productRepository;
    }

    //----- Required Methods -----//

    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        return productRepository.addProduct(product);}

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {
        return productRepository.getProductById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        Product product = productRepository.getProductById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        return productRepository.updateProduct(productId, newName, newPrice);
    }


    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs list cannot be null or empty");
        }

        for (UUID productId : productIds) {
            if (productRepository.getProductById(productId) == null) {
                throw new IllegalArgumentException("Product with ID " + productId + " does not exist");
            }
        }

        productRepository.applyDiscount(discount, productIds);
    }


    public void deleteProductById(UUID productId) {
        Product product = productRepository.getProductById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteProductById(productId);
    }


}