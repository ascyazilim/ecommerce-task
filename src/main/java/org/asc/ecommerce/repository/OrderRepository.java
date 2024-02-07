package org.asc.ecommerce.repository;

import org.asc.ecommerce.entity.Customer;
import org.asc.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}
