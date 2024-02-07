package org.asc.ecommerce.service;

import jakarta.transaction.Transactional;
import org.asc.ecommerce.entity.Customer;
import org.asc.ecommerce.entity.Order;
import org.asc.ecommerce.entity.OrderItem;
import org.asc.ecommerce.entity.Product;
import org.asc.ecommerce.repository.CustomerRepository;
import org.asc.ecommerce.repository.OrderRepository;
import org.asc.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(Long customerId, List<Long> productIds, List<Integer> quantities) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Product IDs and quantities must have the same size.");
        }

        Order order = new Order();
        order.setCustomer(customer);
        double total = 0.0;

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPriceAtOrder(product.getPrice());

            order.getItems().add(item);

            total += item.getPriceAtOrder() * item.getQuantity();
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        }

        order.setTotalPrice(total);
        return orderRepository.save(order);
    }

    public Order getOrderForCode(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getAllOrdersForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return orderRepository.findByCustomer(customer);
    }
}
