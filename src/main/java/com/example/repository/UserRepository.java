package com.example.repository;

import com.example.model.User;
import com.example.model.Order;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }


    public User addUser(User user) {
        user.setId(UUID.randomUUID());
        save(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return findAll();
    }

    public User getUserById(UUID userId) {
        ArrayList<User> users = findAll();
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return (user != null) ? user.getOrders() : new ArrayList<>();
    }

    public void addOrderToUser(UUID userId, Order order) {
        ArrayList<User> users = findAll();
        for (User user : users) {
            if (user.getId().equals(userId)) {
                user.getOrders().add(order);
            }
        }
        saveAll(users);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        ArrayList<User> users = findAll();
        for (User user : users) {
            if (user.getId().equals(userId)) {
                user.getOrders().removeIf(order -> order.getId().equals(orderId));
            }
        }
        saveAll(users);
    }

    public void deleteUserById(UUID userId) {
        ArrayList<User> users = findAll();
        users.removeIf(user -> user.getId().equals(userId));
        saveAll(users);
    }
}
