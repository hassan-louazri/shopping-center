package com.storebackend.service;

import com.storebackend.entities.Order;
import com.storebackend.entities.User;
import com.storebackend.models.UserDTO;
import com.storebackend.repository.OrderRepository;
import com.storebackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public User createUser(UserDTO userDTO) {
        // TODO: check user info before saving

        User user = modelMapper.map(userDTO, User.class);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(String id, UserDTO userDTO) {
        // TODO: check user info before updating

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            modelMapper.map(userDTO, user);
            userRepository.save(user);
        }
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<Order> getUserOrders(String userId) {
        
        List<Order> allOrders = orderRepository.findAll();
        List<Order> userOrders = new ArrayList<Order>();

        for(Order order: allOrders) {
            if(order.getUser() != null && order.getUser().equals(userId)) userOrders.add(order);
        }
        return userOrders;
    }
}
