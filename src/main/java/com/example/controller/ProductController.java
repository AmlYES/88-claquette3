package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
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
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/")
    public ArrayList<Product> getProducts(){
        return productService.getProducts();
    }

    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String,Object> body){
        Product existingProduct = productService.getProductById(productId);

        String newName = body.containsKey("newName") ? (String) body.get("newName") : existingProduct.getName();

        double newPrice = body.containsKey("newPrice") ? Double.parseDouble(body.get("newPrice").toString()) : existingProduct.getPrice();

        return productService.updateProduct(productId, newName, newPrice);
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID> productIds){
        productService.applyDiscount(discount, productIds);

        StringBuilder response = new StringBuilder("Discount of " + discount + "% applied to the following products:\n");
        for (UUID id : productIds) {
            Product product = productService.getProductById(id); // Fetch updated product
            if (product != null) {
                response.append("- ").append(product.getName())
                        .append(" (New Price: ").append(product.getPrice()).append(")\n");
            } else {
                response.append("- Product with ID ").append(id).append(" not found.\n");
            }
        }

        return response.toString();
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId){
        Product product = productService.getProductById(productId); // Fetch deleted product

        if (product == null) {
            return "Product with ID " + productId + " not found.";
        }

        productService.deleteProductById(productId);

        return "The Following Product Was Deleted Successfully: \n"
                + "ID: " + product.getId() + "\n"
                + "Name: " + product.getName() + "\n"
                + "Price: " + product.getPrice();
    }

}
