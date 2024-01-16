package com.storebackend.entities;

import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private HashMap<String, Integer> products;
    
    private Double subtotal;
    
    private Double shippingCost;
    
    public Order() {}
    
    public Order(HashMap<String, Integer> products, Double subtotal, Double shippingCost) {
        this.products = products;
        this.subtotal = subtotal;
        this.shippingCost = shippingCost;
    }
    
    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    
    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }
    
}
