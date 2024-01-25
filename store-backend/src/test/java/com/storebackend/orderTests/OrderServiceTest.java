package com.storebackend.orderTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storebackend.entities.Order;
import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.OrderDTO;
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

    @Test
    void newOrderWhenValid() {
        HashMap<String, Integer> orderProducts = new HashMap<>();
        orderProducts.put("productId1", 2);
        orderProducts.put("productId2", 1);
        // Given
        OrderDTO orderDTO = new OrderDTO(
            "userTestId!!",
            orderProducts,
            999.0,
            50.0
        );
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        
        given(productRepository.findById("productId1")).willReturn(Optional.of(new Product()));
        given(productRepository.findById("productId2")).willReturn(Optional.of(new Product()));
        given(orderRepository.save(orderArgumentCaptor.capture())).willReturn(new Order(orderDTO));
        // When
        Order createdOrder = orderService.newOrder(orderDTO);
        // Then
        verify(productRepository).findById("productId1");
        verify(productRepository).findById("productId2");
        verify(orderRepository).save(any(Order.class));

        assertThat(createdOrder).isNotNull();
        assertThat(orderDTO.getProducts()).isEqualTo(orderArgumentCaptor.getValue().getProducts());
        assertThat(createdOrder.getId()).isEqualTo(orderArgumentCaptor.getValue().getId());
    }

    @Test
    void newOrderWhenInvalidProduct() {
        HashMap<String, Integer> orderProducts = new HashMap<>();
        orderProducts.put("nonExistantProductId", 2);

        // Given
        OrderDTO orderDTO = new OrderDTO(
            "userTestId!!",
            orderProducts,
            999.0,
            50.0
        );
        given(productRepository.findById("nonExistantProductId")).willReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> orderService.newOrder(orderDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request.");

    }

    @Test
    void newOrderWhenInvalidData() {
        HashMap<String, Integer> orderProducts = new HashMap<>();
        orderProducts.put("ProductId", 2);

        // Given
        OrderDTO orderDTO = new OrderDTO(
            "userTestId!!",
            orderProducts,
            999.0,
            -50.0
        );
        given(productRepository.findById("ProductId")).willReturn(Optional.of(new Product()));

        // Then
        assertThatThrownBy(() -> orderService.newOrder(orderDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request.");
    }
}
