package com.storebackend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.storebackend.models.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products") 
public class Product {

    @Id
    private String id;
    
    private String name;
    private Double price;
    private String image;
    private Integer quantity;

    public Product(String name, Double price, String image, Integer quantity) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.image = productDTO.getImage();
        this.quantity = productDTO.getQuantity();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getImage() {
        return this.image;
    }
    
    public void setImage(String imgLink) {
        this.image = imgLink;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}