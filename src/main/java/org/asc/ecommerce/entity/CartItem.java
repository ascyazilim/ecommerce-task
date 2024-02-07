package org.asc.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CartItem extends BaseEntity{
    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    private int quantity;
}
