package com.example.orders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
    @JsonProperty("productId")
    private int productId;
    private String destination;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}