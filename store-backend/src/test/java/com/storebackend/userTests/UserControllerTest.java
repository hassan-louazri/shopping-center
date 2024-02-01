package com.storebackend.userTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.storebackend.controller.UserController;
import com.storebackend.service.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;
    
    private final String apiUrl = "/api/v1/users";

    @Test
    void createUserSuccess() {

    }

    @Test
    void createUserBadRequest() {

    }

    @Test
    void createUserAlreadyExists() {

    }

    @Test
    void getUserByIdSuccess() {

    }

    @Test
    void getUserByIdNotFound() {

    }

    @Test
    void updateUserSuccess() {

    }

    @Test
    void updateUserBadRequest() {

    }

    @Test
    void updateUserInvalidArgument() {

    }

    @Test
    void deleteUserSuccess() {

    }

    @Test
    void deleteUserIllegalArgument() {

    }

    @Test
    void deleteUserNotFound() {

    }

    @Test
    void getAllUsers() {

    }

    @Test
    void getUserOrders() {
        
    }
}
