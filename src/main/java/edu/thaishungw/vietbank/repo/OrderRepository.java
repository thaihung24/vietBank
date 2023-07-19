package edu.thaishungw.vietbank.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.thaishungw.vietbank.models.Order;

@Repository
public class OrderRepository {
    private final Map<String, Order> orderMap;

    public OrderRepository() {
        orderMap = new HashMap<>();
        orderMap.put("1689748334682",
                new Order("1689748334682", "NIKE AIR", 50000, 3, 150000));
        orderMap.put("1689763176168",
                new Order("1689763176168", "NIKE AIR", 50000, 3, 150000));
        orderMap.put("1689763272109",
                new Order("1689763272109", "NIKE AIR", 50000, 6, 300000));
        orderMap.put("1689748334683",
                new Order("1689748334682", "NIKE AIR", 50000, 3, 150000));

    }

    public Order findOrderById(String requestId) {
        return orderMap.get(requestId);
    }

    public List<Order> getAllOrder() {
        return List.copyOf(orderMap.values());
    }
}
