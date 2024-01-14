package com.storebackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.storebackend.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
}
