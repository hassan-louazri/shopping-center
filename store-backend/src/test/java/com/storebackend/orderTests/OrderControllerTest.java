package com.storebackend.orderTests;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storebackend.controller.OrderController;
import com.storebackend.entities.Order;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.OrderDTO;
import com.storebackend.service.OrderService;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final String apiUrl = "/api/v1/orders";

    @Test
    void getOrderFound() throws Exception{
        String orderId = "testOrderId123!!!";
        // Given
        given(orderService.getOrder(orderId)).willReturn(new Order());
        // When
        ResultActions response = mockMvc
                        .perform(MockMvcRequestBuilders.get(apiUrl + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    void getOrderNotFound() throws Exception {
        String orderId = "testOrderId123!!!";
        // Given
        given(orderService.getOrder(orderId)).willThrow(new BadRequestException("Invalid Request. Product not found."));
        // When
        ResultActions response = mockMvc
                        .perform(MockMvcRequestBuilders.get(apiUrl + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isNotFound());
    }

    @Test
    void newOrderValidInput() throws Exception {
        // Given
        OrderDTO validOrderDTO = new OrderDTO();
        Order newOrder = new Order(validOrderDTO);
        given(orderService.newOrder(validOrderDTO)).willReturn(newOrder);
        // When
        ResultActions response = mockMvc
                                    .perform(MockMvcRequestBuilders.post(apiUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(validOrderDTO)));
        // Then
        response.andExpect(status().isCreated());
    }

    @Test
    void newOrderInvalidInput() throws Exception {
        // Given
        OrderDTO invalidOrderDTO = new OrderDTO("testUsrr", new HashMap<>(), -100.0, 500.0);
        given(orderService.newOrder(invalidOrderDTO)).willThrow(new BadRequestException("Invalid Request. Number can't be negative."));
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(invalidOrderDTO)));

        
        response.andExpect(status().isBadRequest());
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
