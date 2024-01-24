package com.storebackend.entities;

import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;

@Document(collection = "orders")
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    
    private String user;
    private HashMap<String, Integer> products;
    private Double subtotal;
    private Double shippingCost;
    
    public Order() {}
    
    public Order(String user, HashMap<String, Integer> products, Double subtotal, Double shippingCost) {
        this.user = user;
        this.products = products;
        this.subtotal = subtotal;
        this.shippingCost = shippingCost;
    }
    
    public String getId() {
        return this.id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String userId) {

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
