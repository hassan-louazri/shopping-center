package com.storebackend.service;

import com.storebackend.entities.Order;
import com.storebackend.entities.User;
import com.storebackend.exceptions.BadRequestException;
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
        // check if user already exists
        Optional<User> exists = userRepository.findByMail(userDTO.getMail());
        if(exists.isPresent()) {
            throw new BadRequestException("Invalid Request. User already exists.");
        }

        // check user info (email, phone, country)
        if(!userValidator(userDTO)) {
            throw new BadRequestException("Invalid Request. Please check user data.");
        };

        User user = modelMapper.map(userDTO, User.class);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        Optional<User> found = userRepository.findById(id); 
        if(found.isPresent()) return found;
        else throw new BadRequestException("Invalid Request. User not found.");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(String id, UserDTO userDTO) {
        // check id is valid
        if(id == null || id.isEmpty()) throw new IllegalArgumentException("User ID cannot be null or empty.");
        // Vérifiez les informations de l'utilisateur avant la mise à jour
        if(!userValidator(userDTO)) throw new BadRequestException("Invalid request. Incorrect information was provided.");

        // check user is valid and present
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            modelMapper.map(userDTO, user);
            userRepository.save(user);
        } else {
            throw new BadRequestException("Invalid Request. User with id: " + id + " not found.");
        }
    }

    public Boolean deleteUser(String id) {
        if(id == null || id.isEmpty()) throw new IllegalArgumentException("User ID cannot be null or empty.");
        Optional<User> exists = userRepository.findById(id);
        if(exists.isEmpty()) throw new BadRequestException("Invalid Request. User to delete not found.");
        
        userRepository.deleteById(id);
        return true;
    }

    public List<Order> getUserOrders(String userId) {
        if(userId == null || userId.isEmpty()) throw new IllegalArgumentException("User ID cannot be null or empty.");

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
