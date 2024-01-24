package com.storebackend.service;

import java.util.List;

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
        return productRepository.findById(id).orElse(null);
    }

    public Product addProduct(Product product) {
        // check product exists
        Product existingProduct = productRepository.findByName(product.getName()).orElse(null);
        if(existingProduct != null) {
            throw new BadRequestException("Invalid Request. Product already exists.");
        }
        // check product info before saving
        if(product.getPrice() <= 0 || product.getQuantity() <= 0) {
            throw new BadRequestException("Invalid Request, Please check price and/or quantity values.");
        }

        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(String id, ProductDTO product) {
        // check product info
        if(product.getPrice() <= 0 || product.getQuantity() <= 0) {
            throw new BadRequestException("Invalid Request. Please check price and/or quantity values.");
        }

        Product productToUpdate = productRepository.findById(id).orElse(null);

        if(productToUpdate != null) {
            modelMapper.map(product, productToUpdate);
            productRepository.save(productToUpdate);
        } else {
            throw new BadRequestException("Invalid Request. Product not found.");
        }
    }
}
