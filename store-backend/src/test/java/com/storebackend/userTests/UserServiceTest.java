package com.storebackend.userTests;

import com.storebackend.entities.Order;
import com.storebackend.entities.User;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.UserDTO;
import com.storebackend.repository.OrderRepository;
import com.storebackend.repository.UserRepository;
import com.storebackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserService userService;

    // @BeforeEach
    // void setUp() {
    //     MockitoAnnotations.initMocks(this);
    // }

    @Test
    void createUserWhenInvalidData() {
        // Données utilisateur invalides
        UserDTO userDTO = new UserDTO("John Doe", "password123", "invalidEmail", "123456789", "Morocco", "123 Street");

        // Simuler le comportement du UserRepository pour findByMail
        when(userRepository.findByMail(anyString())).thenReturn(Optional.empty());

        // Assurez-vous que lorsque la méthode createUser est appelée avec des données utilisateur invalides,
        // elle lève une BadRequestException
        assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void createUserAlreadyExists() {
        UserDTO userDTO = new UserDTO("John Doe", "password123", "john@example.com", "123456789", "USA", "123 Street");
        when(userRepository.findByMail(userDTO.getMail())).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void getUserByIdWhenExists() {
        String userId = "someId";
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Optional<User> user = userService.getUserById(userId);

        assertTrue(user.isPresent());
    }

    @Test
    void getUserByIdWhenDoesNotExist() {
        String userId = "nonExistentId";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessageContaining("Invalid Request.");
    }

    @Test
    void getAllUsers() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getAllUsers();

        assertEquals(userList, users);
    }

    @Test
    void updateUserWhenUserDoesNotExist() {
        // Arrange
        String userId = "someId";
        UserDTO userDTO = new UserDTO("John Doe", "password123", "john@example.com", "123456789", "USA", "123 Street");
        when(userRepository.findById(userId)).thenReturn(Optional.empty()); // Simuler un utilisateur inexistant

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userDTO));
        assertEquals("Invalid Request. User with id: " + userId + " not found.", exception.getMessage());
    }

    @Test
    void updateUserWhenInvalidData() {
        // Arrange
        String userId = "someId";
        UserDTO userDTO = new UserDTO("John Doe", "password123", "john@example.com", "invalidPhone", "USA", "123 Street");
        // Simuler un utilisateur non trouvé dans la base de données
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userDTO));
        assertEquals("Invalid Request. User with id: " + userId + " not found.", exception.getMessage());
    }




    @Test
    void updateUserDoesNotExist() {
        String userId = "nonExistentId";
        UserDTO userDTO = new UserDTO("John Doe", "password123", "john@example.com", "+123456789", "USA", "123 Street");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userDTO));
    }

    @Test
    void deleteUserSuccess() {
        // Given
        given(userRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(new User()));
        // When
        boolean result = userService.deleteUser("randomId123!");
        // Then
        assertTrue(result);
        verify(userRepository, times(1)).deleteById("randomId123!");
    }

    @Test
    void deleteUserNotFound() {
        // Given
        String userId = "randomId124!!";
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> userService.deleteUser(userId))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Invalid Request.");
    }

    @Test
    void deleteUserIllegalArgument() {
        // Given
        String illegalId = null;
        // Then
        assertThatThrownBy(() -> userService.deleteUser(illegalId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID cannot be null or empty.");
    }

    @Test
    void getUserOrdersExists() {
        String userId = "someId";
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> userOrders = userService.getUserOrders(userId);

        assertNotNull(userOrders);
    }

    @Test
    void getUserOrdersDoesNotExist() {
        String userId = "nonExistentId";
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> userOrders = userService.getUserOrders(userId);

        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
    }
}
