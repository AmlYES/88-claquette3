package com.example.model;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Cart {
    private UUID id;
    private UUID userId;
    private List<Product> products=new ArrayList<>();

    public Cart(){

    }

    public Cart(UUID userId) {
        this.userId = userId;
        this.id = UUID.randomUUID();
    }
    // Constructor without id (id will be generated automatically)
    public Cart(UUID userId, List<Product> products) {
        this.id = UUID.randomUUID();
        this.userId=userId;
        this.products=products;

    }
    public Cart(UUID id, UUID userId, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.products = products;

    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Cart:\n" +
                "ID: " + id + "\n" +
                "User: " + userId + "\n" +
                "Products: " + (products.isEmpty() ? "None" : products) + "\n";
    }
}
