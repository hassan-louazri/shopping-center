package com.storebackend.models;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private String name;
    private Double price;
    private String image;
    private Integer quantity;
}
