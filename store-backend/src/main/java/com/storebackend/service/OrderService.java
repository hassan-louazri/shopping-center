package com.storebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storebackend.entities.Order;
import com.storebackend.repository.OrderRepository;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order newOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id).orElse(null);
    }
}
