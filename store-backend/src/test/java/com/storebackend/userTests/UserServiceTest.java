package com.storebackend.userTests;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.storebackend.entities.User;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.UserDTO;
import com.storebackend.repository.OrderRepository;
import com.storebackend.repository.UserRepository;
import com.storebackend.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ModelMapper modelMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        modelMapper = mock(ModelMapper.class);
        userService = new UserService(userRepository, orderRepository);
    }

    @Test
    @Disabled
    void createUserWhenValidData() {

    }

    @Test
    @Disabled
    void createUserWhenInvalidData() {

    }

    @Test 
    void createUserAlreadyExists() {
        // Given
        UserDTO userDTO = new UserDTO();
        given(userRepository.findByMail(userDTO.getMail())).willReturn(Optional.of(new User()));
        // Then
        assertThatThrownBy(() -> userService.createUser(userDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request");
    }

    @Test
    @Disabled
    void getUserByIdWhenExists() {

    }

    @Test
    @Disabled
    void getUserByIdWhenDoesNotExist() {

    }

    @Test
    @Disabled
    void getAllUsers() {

    }

    @Test
    @Disabled
    void updateUserWhenValidData() {

    }

    @Test
    @Disabled
    void updateUserWhenInvalidData() {

    }

    @Test
    @Disabled
    void updateUserDoesNotExist() {

    }

    @Test 
    @Disabled
    void updateUserValidData() {

    }

    @Test
    @Disabled
    void updateUserInvalidData() {

    }

    @Test
    @Disabled
    void deleteUser() {

    }

    @Test
    @Disabled
    void getUserOrdersExists() {

    }

    @Test
    @Disabled
    void getUserOrdersDoesNotExist(){

    }
}
