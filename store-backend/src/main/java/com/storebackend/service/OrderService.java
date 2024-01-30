package com.storebackend.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storebackend.entities.Order;
import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
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

    public Order getOrder(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null) {
            throw new BadRequestException("Invalid Request. Product not found.");
        }
        return order;
    }

    public Order newOrder(OrderDTO orderDTO) { 
        // check order info is valid before saving
        if(orderDTO.getShippingCost() < 0 || orderDTO.getSubtotal() < 0) {
            throw new BadRequestException("Invalid Request. Number can't be negative.");
        }
        
        // check if all products are available
        HashMap<String, Integer> products = orderDTO.getProducts();
        products.forEach((product, quantity) -> {
            Product isAvailable = productRepository.findById(product).orElse(null);
            if(isAvailable == null) {
                throw new BadRequestException(String.format("Invalid Request. The product identified with %s cannot be added to order.", product));
            }
        });
        return orderRepository.save(new Order(orderDTO));
    }

}
