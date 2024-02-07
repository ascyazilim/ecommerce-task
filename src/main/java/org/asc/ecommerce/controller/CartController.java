package org.asc.ecommerce.controller;

import org.asc.ecommerce.entity.Cart;
import org.asc.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long customerId) {
        Cart cart = cartService.getCart(customerId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addProductToCart(@RequestParam Long customerId,
                                                 @RequestParam Long productId,
                                                 @RequestParam int quantity) {
        Cart updatedCart = cartService.addProductToCart(customerId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeProductFromCart(@RequestParam Long cartId,
                                                      @RequestParam Long productId) {
        Cart updatedCart = cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{cartId}/empty")
    public ResponseEntity<Void> emptyCart(@PathVariable Long cartId) {
        cartService.emptyCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cartId}/update")
    public ResponseEntity<Cart> updateCart(@PathVariable Long cartId,
                                           @RequestBody List<Long> productIds,
                                           @RequestBody List<Integer> quantities) {
        Cart updatedCart = cartService.updateCart(cartId, productIds, quantities);
        return ResponseEntity.ok(updatedCart);
    }
}
