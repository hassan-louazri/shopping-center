package com.storebackend.productTests;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.ProductDTO;
import com.storebackend.repository.ProductRepository;
import com.storebackend.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @Disabled
    void getProduct(String id) {

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
    void deleteProduct(String id) {

    }

    @Test
    @Disabled
    void updateProduct(String id, ProductDTO product) {
        
    }

}
