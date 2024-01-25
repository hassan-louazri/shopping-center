package com.storebackend.repository;

import com.storebackend.entities.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByMail(String mail);

}
