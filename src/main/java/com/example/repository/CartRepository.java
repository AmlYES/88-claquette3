package com.example.repository;
import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {


    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public Cart addCart(Cart cart){
        cart.setId(UUID.randomUUID());
        save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts(){
        return findAll();
    }

    public Cart getCartById(UUID cartId){
        ArrayList<Cart> carts = findAll();
        for(Cart cart: carts){
            if(cart.getId().equals(cartId)){
                return cart;
            }
        }
        return null;
    }

    public Cart getCartByUserId(UUID userId){
        ArrayList<Cart> carts = findAll();
        for(Cart cart: carts){
            if(cart.getUserId().equals(userId)){
                return cart;
            }
        }
        return null;
    }
    public void addProductToCart(UUID cartId, Product product){
        ArrayList<Cart> carts= findAll();
        for(Cart cart: carts){
            if(cart.getId().equals(cartId)){
                cart.getProducts().add(product);
            }
        }
        saveAll(carts);
    }
    public void deleteProductFromCart(UUID cartId, Product product){
        ArrayList<Cart> carts = findAll();
        for(Cart cart: carts){
//            if(cart.getId().equals(cartId)){
//                cart.getProducts().remove(product);
//            }
            //cart.getProducts().removeIf(p -> p.getId().equals(product.getId())); //removes all instances of the product in that cart

            // to remove only the first occurrence of the product in the cart
            List<Product> products = cart.getProducts();
            Iterator<Product> iterator = products.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId().equals(product.getId())) {
                    iterator.remove();
                    break;
                }
            }
        }
        saveAll(carts);
    }
    public void deleteCartById(UUID cartId){
        ArrayList<Cart> carts= findAll();
//        for(Cart cart: carts){
//            if(cart.getId().equals(cartId)){
//                carts.remove(cart);
//                break;
//            }
//        }
        carts.removeIf(cart -> cart.getId().equals(cartId));
        saveAll(carts);
    }

    public void emptyCart(UUID userId) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getUserId().equals(userId)) {
                cart.getProducts().clear();
            }
        }
        saveAll(carts);
    }
}