package com.storebackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.storebackend.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

    Optional<Product> findByName(String name);
    
}
