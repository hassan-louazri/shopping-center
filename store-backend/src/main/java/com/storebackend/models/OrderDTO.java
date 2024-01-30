package com.storebackend.models;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String user;
    private HashMap<String, Integer> products;
    private Double subtotal;
    private Double shippingCost;

    public String getUserId() {
        return this.user;
    }

    public HashMap<String, Integer> getProducts() {
        return this.products;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public Double getShippingCost() {
        return this.shippingCost;
    }
}
