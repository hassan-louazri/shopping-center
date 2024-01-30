package com.storebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storebackend.entities.Order;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.OrderDTO;
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
    public ResponseEntity<?> getOrder(@PathVariable String id) throws Exception {
        try {
            Order order = orderService.getOrder(id);
            return ResponseEntity.ok(order);
        } catch (BadRequestException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occured.");
        }
    }

    @PostMapping
    public ResponseEntity<?> newOrder(@RequestBody OrderDTO orderDTO) throws Exception {
        try {
            Order newOrder = orderService.newOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Invalid Request. Please Check DTO");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occured.");
        }
    }
}
