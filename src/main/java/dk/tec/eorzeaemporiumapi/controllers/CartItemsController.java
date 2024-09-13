package dk.tec.eorzeaemporiumapi.controllers;

import dk.tec.eorzeaemporiumapi.models.CartItem;
import dk.tec.eorzeaemporiumapi.models.Product;
import dk.tec.eorzeaemporiumapi.repositories.CartItemsRepository;
import dk.tec.eorzeaemporiumapi.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartItems")
public class CartItemsController {

    private final CartItemsRepository cartItemsRepo;
    private final ProductRepository productRepo;

    CartItemsController(CartItemsRepository cartItemsRepo, ProductRepository productRepo) {
        this.cartItemsRepo = cartItemsRepo;
        this.productRepo = productRepo;
    }

    @GetMapping
    List<CartItem> getAll() {
        List<CartItem> cartItems = cartItemsRepo.findAll();
        return cartItems;
    }

    @GetMapping("/{id}")
    ResponseEntity<CartItem> getById(@PathVariable int id) {
        return cartItemsRepo.findById(id)
                .map(cartItem -> ResponseEntity.ok(cartItem))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    ResponseEntity<CartItem> create(@RequestBody CartItem cartItem) {
        Product product = productRepo.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        cartItem.setProduct(product);
        CartItem savedCartItem = cartItemsRepo.save(cartItem);
        return ResponseEntity.ok(savedCartItem);
    }

    @PutMapping("/{id}")
    CartItem update(@PathVariable int id, @RequestBody CartItem cartItem) {
        return cartItemsRepo.findById(id)
                .map(existingCartItem -> {
                    Product product = productRepo.findById(cartItem.getProduct().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    existingCartItem.setProduct(product);
                    existingCartItem.setQuantity(cartItem.getQuantity());
                    return cartItemsRepo.save(existingCartItem);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> delete(@PathVariable int id) {
        return cartItemsRepo.findById(id)
                .map(cartItem -> {
                    cartItemsRepo.delete(cartItem);
                    return ResponseEntity.ok(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    ResponseEntity<HttpStatus> deleteAll() {
        cartItemsRepo.deleteAll();
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
