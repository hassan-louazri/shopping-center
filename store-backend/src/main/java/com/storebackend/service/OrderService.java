package com.storebackend.service;

import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storebackend.entities.Order;
import com.storebackend.entities.Product;
import com.storebackend.repository.OrderRepository;
import com.storebackend.repository.ProductRepository;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order newOrder(Order order) {

        HashMap<String, Integer> products = order.getProducts();
        // check if all products are available
        products.forEach((product, quantity) -> {
            Product isAvailable = productRepository.findById(product).orElse(null);
            if(isAvailable == null) return;
        });


        return orderRepository.save(order);
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id).orElse(null);
    }
}
