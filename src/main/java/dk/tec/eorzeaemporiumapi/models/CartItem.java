package dk.tec.eorzeaemporiumapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue
    int id;
    int quantity;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
