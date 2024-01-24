package com.storebackend.productTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storebackend.entities.Order;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.repository.OrderRepository;
import com.storebackend.repository.ProductRepository;
import com.storebackend.service.OrderService;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productRepository);
    }

    @Test
    void getOrderWhenExists() {
        // Given
        String orderId = "testOrderId123!";
        String userId = "testUserId";
        HashMap<String, Integer> orderProducts = new HashMap<>();
        Order expectedOrder = new Order(
            orderId, 
            userId, 
            orderProducts, 
            499.0, 
            50.0
        );
        given(orderRepository.findById(orderId)).willReturn(Optional.of(expectedOrder));
        // When
        Order actualOrder = orderService.getOrder(orderId);
        //Then
        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void getOrderWhenDoesNotExist() {
        // Given
        String orderId = "testOrderId123!";
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> orderService.getOrder(orderId))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request.");

        verify(orderRepository).findById(orderId);
    }
}
