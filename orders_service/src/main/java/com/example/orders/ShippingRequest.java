package com.example.orders;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShippingRequest(
    @JsonProperty("product_id") int product_id,
    String destination
) {}
