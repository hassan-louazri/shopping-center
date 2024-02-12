package com.storebackend.userTests;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storebackend.controller.UserController;
import com.storebackend.entities.User;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.UserDTO;
import com.storebackend.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
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
    void createUserSuccess() throws Exception {
        UserDTO validDTO = new UserDTO(
            "testName",
            "password123",
            "random@mail.com",
            "0588212502",
            "USA",
            "15 Random street, State, 52022"
        );
        User newUser = new User(validDTO);
        // Given
        given(userService.createUser(validDTO)).willReturn(newUser);
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(validDTO)));
        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newUser.getName()))
                .andExpect(jsonPath("$.mail").value(newUser.getMail()))
                .andExpect(jsonPath("$.phone").value(newUser.getPhone()));
    }

    @Test
    void createUserBadRequest() throws Exception {
        // Arrange
        UserDTO invalidUserDTO = new UserDTO("testName",
        "password123",
        "",
        "0588212502",
        "USA",
        "15 Random street, State, 52022");

        given(userService.createUser(invalidUserDTO)).willThrow(BadRequestException.class);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidUserDTO)));

        // Assert
        resultActions.andExpect(status().isBadRequest())
                     .andExpect(content().string("Invalid Request: Cannot create new User."));
    }

    @Test
    void createUserAlreadyExists() throws Exception {
        UserDTO alreadyExistsDto = new UserDTO("testName",
        "password123",
        "user@test.com",
        "0588212502",
        "USA",
        "15 Random street, State, 52022");

        // Given
        given(userService.createUser(alreadyExistsDto)).willThrow(BadRequestException.class);
        //Then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(alreadyExistsDto)));

        // Assert
        resultActions.andExpect(status().isBadRequest())
                     .andExpect(content().string("Invalid Request: Cannot create new User."));
    }

    @Test
    void getUserByIdSuccess() throws Exception {
        String userId = "testUserId123!!";
        // Given
        given(userService.getUserById(userId)).willReturn(Optional.of(new User()));
        //When
        ResultActions response = mockMvc
                        .perform(MockMvcRequestBuilders.get(apiUrl + "/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    void getUserByIdNotFound() throws Exception {
        String orderId = ArgumentMatchers.anyString();
        // Given
        given(userService.getUserById(orderId)).willThrow(BadRequestException.class);
        // When
        ResultActions response = mockMvc
                        .perform(MockMvcRequestBuilders.get(apiUrl + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isNotFound());
        // Then
    }

    @Test
    void updateUserSuccess() throws Exception {
        // Given
        String userId = "testUserId123!!";
        UserDTO validUserDto = new UserDTO("testName",
                                        "password123",
                                        "email@company.com",
                                        "0588212502",
                                        "USA",
                                        "15 Random street, State, 52022");
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(apiUrl + "/{id}", userId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(asJsonString(validUserDto)));
        // Then
        response.andExpect(status().isNoContent());
    }

    @Test
    void updateUserBadRequest() throws Exception {
        // Given
        String userId = "testUserId123!!";
        // invalid dto or user not found
        UserDTO invalidUserDto = new UserDTO("testName",
                                        "password123",
                                        "wrongemail@com",
                                        "0502",
                                        "USA",
                                        "15 Random street, State, 52022"
                                        );
        // When

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(apiUrl + "/{id}", userId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(asJsonString(invalidUserDto)));
        // Then
        response.andExpect(status().isBadRequest());
    }

    // @Test
    // void updateUserInvalidArgument() throws Exception {
    //             // Given
    //             String userId = null;
    //             // invalid dto or user not found
    //             UserDTO validUserDto = new UserDTO("testName",
    //                                             "password123",
    //                                             "wrongemail@com",
    //                                             "0588212502",
    //                                             "USA",
    //                                             "15 Random street, State, 52022"
    //                                             );
    //             // When
    //             ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(apiUrl + "/{id}", userId)
    //                                             .contentType(MediaType.APPLICATION_JSON)
    //                                             .content(asJsonString(validUserDto)));
    //             // Then
    //             response.andExpect(status().isBadRequest());                
    // }

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

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
