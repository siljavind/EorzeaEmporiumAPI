package dk.tec.eorzeaemporiumapi.repositories;

import dk.tec.eorzeaemporiumapi.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItem, Integer> {
}
