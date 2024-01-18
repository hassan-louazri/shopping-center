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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        // check user info
        if(!userValidator(userDTO)) return new User();
        
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

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            modelMapper.map(userDTO, user);
            //check user info before updating
            if(userValidator(userDTO)){
                userRepository.save(user);
            } else {
                return;
            }
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

    private boolean userValidator(UserDTO userDTO) {
        return validateEmail(userDTO.getMail()) && validatePhone(userDTO.getPhone()) && validateCountry(userDTO.getCountry());
    }

    private boolean validateEmail(String mail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    private boolean validatePhone(String phone) {
        String phoneRegex = "^(\\\\+\\\\d{1,3}[- ]?)?\\\\d{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private boolean validateCountry(String country) {
        //TODO: change
        String[] availableCountries = {"Morocco", "France", "USA", "UK"};
        for(String availableCountry : availableCountries) {
            if(country.equalsIgnoreCase(availableCountry)) return true;
        }

        return false;
    }
}
