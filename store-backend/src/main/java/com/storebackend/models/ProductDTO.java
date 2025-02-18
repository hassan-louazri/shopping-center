package com.storebackend.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    @Getter
    private String name;
    @Getter
    private Double price;
    @Getter
    private String image;
    @Getter
    private Integer quantity;
}
