package org.asc.ecommerce.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Product  extends BaseEntity{
    private String name;
    private double price;
    private int stockQuantity;
}
