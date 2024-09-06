package dk.tec.eorzeaemporiumapi.controllers;

import dk.tec.eorzeaemporiumapi.models.Product;
import dk.tec.eorzeaemporiumapi.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repo;

    ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    ResponseEntity<Iterable<Product>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Product> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product savedProduct = repo.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    //TODO Add check if path variable id is the same as product id (if providing id in body)
    @PutMapping("/{id}")
    ResponseEntity<Product> update(@PathVariable int id, @Valid @RequestBody Product product) {
        return repo.findById(id)
                .map(existingProduct -> {
                    BeanUtils.copyProperties(product, existingProduct, "id");
                    return ResponseEntity.ok(repo.save(existingProduct));
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable int id) {
        return repo.findById(id)
                .map(product -> {
                    repo.delete(product);
                    return ResponseEntity.ok("Product deleted successfully");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"));
    }
}