package com.storebackend.productTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.ProductDTO;
import com.storebackend.repository.ProductRepository;
import com.storebackend.service.ProductService;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper mockMapper;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        mockMapper = mock(ModelMapper.class);
        productService = new ProductService(productRepository, mockMapper);
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
        given(productRepository.findById(productId)).willReturn(Optional.of(expectedProduct));
        // When
        Product actualProduct = productService.getProduct(productId);
        // Then
        assertThat(actualProduct).isNotNull();
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    void getProductWhenDoesNotExist() {
        // Given
        String productId = "123productTestId!!!";
        given(productRepository.findById(productId)).willReturn(Optional.empty());
        // When
        Product actualProduct = productService.getProduct(productId);
        // Then
        assertThat(actualProduct).isNull();
    }

    @Test
    // @Disabled
    void addProductWhenValidData() {
        // Given
        ProductDTO productDTO = new ProductDTO(
            "TestProduct",
            999.0,
            "http://linkimage.com",
            1
        );
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        
        given(productRepository.findByName("TestProduct")).willReturn(Optional.empty());
        given(productRepository.save(productCaptor.capture())).willReturn(new Product(productDTO));

        // When
        Product addedProduct = productService.addProduct(productDTO);

        // Then
        Product capturedProduct = productCaptor.getValue();
        verify(productRepository).findByName("TestProduct");
        verify(productRepository).save(any(Product.class));
        
        assertThat(addedProduct).isNotNull();
        assertThat(addedProduct.getId()).isEqualTo(capturedProduct.getId());
    }

    @Test
    void addProductAlreadyExists() {
        ProductDTO existingProductDTO = new ProductDTO(
            "Test Product",
            366.0,
            "http://imageLink.com",
            1
        );
        ProductDTO newProductDTO = new ProductDTO(
            "Test Product",
            450.0,
            "http://newimagelink.com",
            1
        );

        Product existingProduct = new Product(existingProductDTO);
        // Given
        given(productRepository.findByName("Test Product")).willReturn(Optional.of(existingProduct));
        // Then
        assertThatThrownBy(() -> productService.addProduct(newProductDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request.");
        
        verify(productRepository).findByName(existingProductDTO.getName());
    }

    @Test
    void addProductWhenInvalidData() {
        // Given
        ProductDTO productDTO = new ProductDTO(
            "TestProduct",
            -1.99,
            "http://imagelink.com",
            1
        );
        
        // Then
        assertThatThrownBy(() -> productService.addProduct(productDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request. Please check price and/or quantity values.");
    }

    @Test
    void deleteProductSuccess() {
        // Given
        String productId = "123productTestId!!!";
        given(productRepository.findById(productId)).willReturn(Optional.of(new Product()));
        // When 
        Boolean result = productService.deleteProduct(productId);
        // Then
        verify(productRepository).deleteById(productId);
        assertThat(result).isTrue();
    }

    @Test 
    void deleteProductWrongId() {
        assertThatThrownBy(() -> productService.deleteProduct(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product ID cannot be null or empty");
    }

    @Test
    void deleteProductNotFound() {
        String productId = "456";
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // When and Then
        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid Request. Product to delete not found");
    }
    @Test
    void updateProductWhenValidData() {
        // Given
        String productId = "123productTestId!!!";
        Product existingProduct = new Product(
            productId, 
            "Test Product",
            367.0,
            "http://imageLink.com",
            2
        );
        ProductDTO updatedDTO = new ProductDTO(
            "Updated Test Product",
            366.0,
            "http://imageLink.com",
            1
        );
        given(productRepository.findById(productId)).willReturn(Optional.of(existingProduct));
        // When
        productService.updateProduct(productId, updatedDTO);
        // Then
        verify(productRepository).findById(productId);
        verify(mockMapper).map(updatedDTO, existingProduct);
        verify(productRepository).save(existingProduct);
    }

    @Test 
    void updateProductWhenDoesNotExist() {
        // Given
        String productId = "123productTestId!!!";
        ProductDTO updateDTO = new ProductDTO(
            productId, 
            367.0,
            "http://imageLink.com",
            1    
        );
        given(productRepository.findById(productId)).willReturn(Optional.empty());
        
        // Then
        assertThatThrownBy(() -> productService.updateProduct(productId, updateDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request.");
        
        verify(productRepository).findById(productId);
    }

    @Test
    void updateProductWhenInvalidData() {
        String productId = "123productTestId!!!";
        ProductDTO updatedDTO = new ProductDTO(
            productId, 
            -367.0,
            "http://imageLink.com",
            -10    
        );
        assertThatThrownBy(() -> productService.updateProduct(productId, updatedDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Invalid Request.");
    }
}
