package com.storebackend.productTests;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storebackend.controller.ProductController;
import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.ProductDTO;
import com.storebackend.service.ProductService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final String apiUrl = "/api/v1/products";

    @Test
    @WithMockUser
    void getProducts() throws Exception {
        List<Product> products = Arrays.asList(
            new Product("testProduct1", 999.0, "http://imagelink.com", 50),
            new Product("testProduct2", 999.1, "http://imagelink.com", 50)
        ); 

        // Given
        given(productService.getProducts()).willReturn(products);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(apiUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(products.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("testProduct1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(999.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("testProduct2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(999.1));
    }

    @Test
    @WithMockUser
    void getProductFound() throws Exception {
        // Given
        String productId = "productId123!!!";
        Product product = new Product("testProduct2", 999.1, "http://imagelink.com", 50);
        given(productService.getProduct(productId)).willReturn(product);

        // Then 
        mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()));
    }

    @Test
    @WithMockUser
    void getProductNotFound() throws Exception {
        // Given
        given(productService.getProduct("1")).willReturn(null);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/1")
                                        .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createProductValidData() throws Exception {
        ProductDTO productDTO = new ProductDTO("testProduct", 99.0, "http//imagelink.com", 50);
        Product newProduct = new Product(productDTO);
        // Given
        given(productService.addProduct(ArgumentMatchers.any())).willReturn(newProduct);
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(productDTO))
                                    );
        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newProduct.getName()))
                .andExpect(jsonPath("$.price").value(newProduct.getPrice()));
    }

    @Test
    @WithMockUser
    void createProductInvalidData() throws Exception {
        ProductDTO invalidProductDTO = new ProductDTO("null", -100.0, "null", 50);
        // Given
        given(productService.addProduct(ArgumentMatchers.any())).willThrow(new BadRequestException("Invalid Request. Please Check DTO."));
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(invalidProductDTO)));
                                    
        // Then
        response.andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid Request. Please Check DTO."));
    }

    @Test
    @WithMockUser
    void createProductUnexpectedError() throws Exception {
        ProductDTO productDTO = new ProductDTO("null", 100.0, "null", 50);
        // Given
        when(productService.addProduct(ArgumentMatchers.any())).thenThrow(new RuntimeException("An unexpected error occurred"));
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(productDTO))
        );
        // Then
        response.andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("An unexpected error occurred"));
    }

    @Test
    @WithMockUser
    void updateProductValidData() throws Exception {
        String idToUpdate = "testProductId123!!!";
        ProductDTO productDTO = new ProductDTO("null", 100.0, "null", 50);
        // Given
        given(productService.updateProduct(idToUpdate, productDTO)).willReturn(true);
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", idToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)));
        // Then
        response.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void updateProductInvalidData() throws Exception {
        String idToUpdate = "testProductId123!!!";
        ProductDTO invalidProductDTO = new ProductDTO("", 100.0, "null", 50);
        // Given
        given(productService.updateProduct(idToUpdate, invalidProductDTO))
                            .willThrow(new BadRequestException("Invalid Request. Please Check DTO."));
        
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", idToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidProductDTO)));
        // Then
        response.andExpect(status().isBadRequest());
    
    }

    @Test
    @WithMockUser
    void updateProductNotFound() throws Exception{
        ProductDTO invalidProductDTO = new ProductDTO("", 100.0, "null", 50);
        // Given
        given(productService.updateProduct("", invalidProductDTO))
                            .willThrow(new IllegalArgumentException("Product ID cannot be null or empty"));
        
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidProductDTO)));
        // Then
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateProductUnexpectedError() throws Exception {
        // Given
        String productId = "123";
        ProductDTO validProductDTO = new ProductDTO("Valid Product", 99.0, "http://valid-image.com", 50);

        given(productService.updateProduct(productId, validProductDTO))
                .willThrow(new RuntimeException("Unexpected error occurred."));

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(validProductDTO)));

        // Then
        response.andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void deleteProductExists() throws Exception{
        // Given
        String productId = "123";

        // Mock the service to handle the delete
        given(productService.deleteProduct(productId)).willReturn(true);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", productId));

        // Then
        response.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deleteProductInvalidId() throws Exception{
        // Given
        String nonValidProductId = "";

        // Mock the service to throw an IllegalArgumentException for a bad identification
        given(productService.deleteProduct(nonValidProductId))
        .willThrow(new IllegalArgumentException("Product ID cannot be null or empty"));
        
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", nonValidProductId));

        // Then
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteProductNonExistant() throws Exception{
        String nonExistingProductId = "noProductid126";
        // Given
        given(productService.deleteProduct(nonExistingProductId))
        .willThrow(new BadRequestException("Invalid Request. Product to delete not found\""));
        
        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", nonExistingProductId));

        // Then
        response.andExpect(status().isBadRequest());
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
