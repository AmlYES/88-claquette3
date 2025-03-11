package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    //The Dependency Injection Variables
    //The Constructor with the requried variables mapping the Dependency Injection.

    //product dependent only
    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        try {
            return productService.addProduct(product);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        try {
            return productService.getProductById(productId);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return  null;
        }
    }

    @GetMapping("/")
    public ArrayList<Product> getProducts(){
        return productService.getProducts();
    }

    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
        try {
            Product existingProduct = productService.getProductById(productId);

            String newName = body.containsKey("newName") ? (String) body.get("newName") : existingProduct.getName();
            double newPrice = body.containsKey("newPrice") ? Double.parseDouble(body.get("newPrice").toString()) : existingProduct.getPrice();

            return productService.updateProduct(productId, newName, newPrice);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return null;
        }
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount, @RequestBody ArrayList<UUID> productIds) {
        try {
            productService.applyDiscount(discount, productIds);
            return "Discount applied successfully";
        } catch (IllegalArgumentException e) {
            return "Invalid discount request: " + e.getMessage();
        }
    }
//    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID> productIds){
//        productService.applyDiscount(discount, productIds);
//
////        StringBuilder response = new StringBuilder("Discount applied successfully");
////        for (UUID id : productIds) {
////            Product product = productService.getProductById(id); // Fetch updated product
////            if (product != null) {
////                response.append("- ").append(product.getName())
////                        .append(" (New Price: ").append(product.getPrice()).append(")\n");
////            } else {
////                response.append("- Product with ID ").append(id).append(" not found.\n");
////            }
////        }
//
//        return "Discount applied successfully";
//    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        try {
            productService.getProductById(productId); // Ensure product exists
            productService.deleteProductById(productId);
            return "Product deleted successfully";
        } catch (IllegalArgumentException e) {
            return ("Invalid product ID: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ("Product not found: " + e.getMessage());
        }
    }
}


