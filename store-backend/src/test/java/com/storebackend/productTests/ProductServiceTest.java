package com.storebackend.productTests;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.repository.ProductRepository;
import com.storebackend.service.ProductService;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, null);
    }


    @Test
    void getProducts() {
        // When
        productService.getProducts();
        //Then
        verify(productRepository).findAll();
    }

    @Test
    void getProductWhenExists() {
        // Given
        String productId = "123productTestId!!!";
        Product expectedProduct = new Product(
            productId,
            "TestProduct",
            999.0,
            "http://linkimage.com",
            1
        );
        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));
        // When
        Product actualProduct = productService.getProduct(productId);
        // Then
        assertNotNull(actualProduct);
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    @Disabled
    void getProductWhenDoesNotExist() {
        // When
        

        // Then
    }

    @Test
    void addProduct() {
        // Given
        Product product = new Product(
            "TestProduct",
            999.0,
            "http://linkimage.com",
            1
        );
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        
        // When
        productService.addProduct(product);
        // Then
        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct).isEqualTo(product);
    }

    @Test
    void addProductException() {
        // Given
        Product product = new Product(
            "TestProduct",
            -1.99,
            "http://imagelink.com",
            1
        );
        
        // Then
        assertThatThrownBy(() -> productService.addProduct(product))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request, please check product structure");
    }

    @Test
    @Disabled
    void deleteProduct() {

    }

    @Test
    @Disabled
    void updateProduct() {
        
    }

}
