package com.storebackend.models;
import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private Double price;
    private String image;
    private Integer quantity;
}
