package dk.tec.eorzeaemporiumapi.repositories;

import dk.tec.eorzeaemporiumapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
