package dk.tec.eorzeaemporiumapi;

import dk.tec.eorzeaemporiumapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EorzeaEmporiumRepository extends JpaRepository<Product, Integer> {
}
