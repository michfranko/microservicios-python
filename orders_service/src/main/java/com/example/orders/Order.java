package com.example.orders;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders_tbl") // ← nombre seguro
public class Order extends PanacheEntity {

    @Column(name = "product_ids", nullable = false)
    public String productIds;

    @Column(nullable = false)
    public String destination;

    @Column(name = "shipping_cost", nullable = false)
    public Double shippingCost;

    @Column(nullable = false)
    public Double total;

    // Getters y Setters
    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
