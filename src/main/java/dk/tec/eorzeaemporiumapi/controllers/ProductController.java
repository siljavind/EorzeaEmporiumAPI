package dk.tec.eorzeaemporiumapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.tec.eorzeaemporiumapi.models.Product;
import dk.tec.eorzeaemporiumapi.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repo;
    private final ObjectMapper objectMapper;

    ProductController(ProductRepository repo, ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Product> create(@Valid @RequestPart("product") Product product, @RequestPart("image") MultipartFile image) throws IOException {
        try {
            product.setImage(image.getBytes());
            Product savedProduct = repo.save(product);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedProduct.getId())
                    .toUri();

            return ResponseEntity.created(location).body(savedProduct);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO Add check if path variable id is the same as product id (if providing id in body)
    @PutMapping(value = "/{id}")
    ResponseEntity<Product> update(@PathVariable int id, @Valid @RequestBody Product product) {
        return repo.findById(id)
                .map(existingProduct -> {
                    try {
                        BeanUtils.copyProperties(product, existingProduct, "id, image");
                        return ResponseEntity.ok(repo.save(existingProduct));
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(existingProduct);
                    }

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