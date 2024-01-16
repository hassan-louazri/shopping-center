package com.storebackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.storebackend.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {}
