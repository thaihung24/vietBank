package edu.thaishungw.vietbank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.thaishungw.vietbank.models.Order;

import edu.thaishungw.vietbank.repo.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrderById(String requestId) {
        return orderRepository.findOrderById(requestId);
    }

    public List<Order> getAllOrder() {
        return orderRepository.getAllOrder();
    }
}
