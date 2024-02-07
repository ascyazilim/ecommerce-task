package org.asc.ecommerce.service;

import org.asc.ecommerce.entity.Cart;
import org.asc.ecommerce.entity.CartItem;
import org.asc.ecommerce.entity.Customer;
import org.asc.ecommerce.entity.Product;
import org.asc.ecommerce.repository.CartItemRepository;
import org.asc.ecommerce.repository.CartRepository;
import org.asc.ecommerce.repository.CustomerRepository;
import org.asc.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       CustomerRepository customerRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart getCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Cart cart = customer.getCart();
        if (cart == null) {
            throw new RuntimeException("Cart not found for the given customer");
        }
        return cart;
    }

    @Transactional
    public Cart addProductToCart(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock for the product");
        }

        Cart cart = customer.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cartRepository.save(cart);
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        return updateCartTotalPrice(cart);
    }

    @Transactional
    public Cart removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in the cart"));

        productRepository.findById(productId).ifPresent(product -> {
            product.setStockQuantity(product.getStockQuantity() + itemToRemove.getQuantity());
            productRepository.save(product);
        });

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        return updateCartTotalPrice(cart);
    }

    @Transactional
    public void emptyCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().forEach(cartItem -> {
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() + cartItem.getQuantity());
            productRepository.save(product);
        });
        cart.getItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);
    }

    private Cart updateCartTotalPrice(Cart cart) {
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCart(Long cartId, List<Long> productIds, List<Integer> quantities) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        emptyCart(cartId);
        for (int i = 0; i < productIds.size(); i++) {
            addProductToCart(cart.getCustomer().getId(), productIds.get(i), quantities.get(i));
        }
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found after update"));
    }
}
