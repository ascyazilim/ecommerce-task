package org.asc.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class OrderItem extends BaseEntity{

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private double priceAtOrder;
    private int quantity;


}
