package com.example.controller;

import com.example.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final Product product;

    public ProductController(Product product) {
        this.product = product;
    }
    //The Dependency Injection Variables
    //The Constructor with the requried variables mapping the Dependency Injection.

    //product dependent only
    @PostMapping("/")
    public Product addProduct(@RequestBody Product product){
        return product;
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId){
        return product;
    }

    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String,Object> body){
        return product;
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID> productIds){
        return "";
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId){
        return "";
    }

}
