package dk.tec.eorzeaemporiumapi;

import dk.tec.eorzeaemporiumapi.models.Product;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class EorzeaEmporiumController {

    EorzeaEmporiumRepository repo;

    EorzeaEmporiumController(EorzeaEmporiumRepository repo) { this.repo = repo; }

    @GetMapping()
    Iterable<Product> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Product> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    ResponseEntity<String> create(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = repo.save(product);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedProduct.getId())
                    .toUri();

            return ResponseEntity.created(location).body("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Product");
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Product> update(@PathVariable int id, @Valid @RequestBody Product product) {
        return repo.findById(id)
                .map(existingProduct -> {
                    if (product.getId() != id) return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
                    BeanUtils.copyProperties(product, existingProduct, "id");
                    return ResponseEntity.ok(repo.save(existingProduct));
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
