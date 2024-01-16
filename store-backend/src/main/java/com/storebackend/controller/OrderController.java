package com.storebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storebackend.entities.Order;
import com.storebackend.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id);

        if(order != null) return ResponseEntity.ok(order);
        else return ResponseEntity.notFound().build(); 
    }

    @PostMapping
    public ResponseEntity<Order> newOrder(@RequestBody Order order) {
        Order newOrder = orderService.newOrder(order);

        return ResponseEntity.ok(newOrder);
    }
}
