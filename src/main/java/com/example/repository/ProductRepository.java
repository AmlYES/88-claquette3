package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")


public class ProductRepository extends MainRepository<Product> {

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }

    @Override
    protected Class<Product[]> getArrayType() { return Product[].class; }

    //----- Required Methods -----//

    public Product addProduct(Product product) {
        product.setId(UUID.randomUUID());
        save(product);
        return product;
    }

    public ArrayList<Product> getProducts() { return findAll();}

    public Product getProductById(UUID productId) {
        ArrayList<Product> products = findAll();
        for(Product product: products){
            if(product.getId().equals(productId)){
                return product;
            }
        }
        return null;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                saveAll(products);
                return product;
            }
        }
        return null;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();
        for(Product product: products){
            if(productIds.contains(product.getId())){
                double discountedPrice = product.getPrice()- ( ( discount/100) * product.getPrice());
                product.setPrice(discountedPrice);
            }
        }
        saveAll(products);
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();
//        for(Product product: products){
//            if(productId.equals(product.getId())){
//                products.remove(product);
//            }
//        }
        products.removeIf(product -> product.getId().equals(productId));
        saveAll(products);
    }
}
