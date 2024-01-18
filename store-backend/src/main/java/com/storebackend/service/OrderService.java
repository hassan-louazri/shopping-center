package com.storebackend.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storebackend.entities.Order;
import com.storebackend.entities.Product;
import com.storebackend.models.OrderDTO;
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

    public Order newOrder(OrderDTO orderDTO) {
      
        // check if all products are available
        HashMap<String, Integer> products = orderDTO.getProducts();
        products.forEach((product, quantity) -> {
            Product isAvailable = productRepository.findById(product).orElse(null);
            if(isAvailable == null) return;
        });
        
        
        Order newOrder = new Order(orderDTO.getUser(), products, orderDTO.getSubtotal(), orderDTO.getShippingCost());
        // Order newOrder = new Order(user, products, orderDTO.getSubtotal(), orderDTO.getShippingCost());

        return orderRepository.save(newOrder);
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id).orElse(null);
    }
}
