package com.storebackend.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storebackend.entities.Product;
import com.storebackend.exceptions.BadRequestException;
import com.storebackend.models.ProductDTO;
import com.storebackend.repository.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;

    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        return productRepository.findById(id).orElse(null);
    }

    public Product addProduct(ProductDTO productDTO) {
        // check if product already exists
        Product existingProduct = productRepository.findByName(productDTO.getName()).orElse(null);
        if(existingProduct != null) {
            throw new BadRequestException("Invalid Request. Product already exists.");
        }
        // check product info before saving
        if(productDTO.getPrice() <= 0 || productDTO.getQuantity() <= 0) {
            throw new BadRequestException("Invalid Request. Please check price and/or quantity values.");
        }
        Product newProduct = new Product(productDTO);
        return productRepository.save(newProduct);
    }

    public Boolean deleteProduct(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        
        Optional<Product> exists = productRepository.findById(id);
        if(exists.isEmpty()){
            throw new BadRequestException("Invalid Request. Product to delete not found");
        }

        productRepository.deleteById(id);
        return true;
    }

    public Boolean updateProduct(String id, ProductDTO product) {
        // check product info
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if(product.getPrice() <= 0 || product.getQuantity() <= 0) {
            throw new BadRequestException("Invalid Request. Please check price and/or quantity values.");
        }

        Product productToUpdate = productRepository.findById(id).orElse(null);

        if(productToUpdate != null) {
            modelMapper.map(product, productToUpdate);
            productRepository.save(productToUpdate);
            return true;
        } else {
            throw new BadRequestException("Invalid Request. Product not found.");
        }
    }
}
