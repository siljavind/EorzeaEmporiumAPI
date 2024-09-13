package dk.tec.eorzeaemporiumapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    int id;

    @NotBlank
    String name;

    @NotNull
    @Lob
    String description;

    @PositiveOrZero
    int price;

    @Lob
    byte[] image;

    @PositiveOrZero
    int stock;

    @NotNull
    Category category;


//    boolean isAvailable;
//    boolean isCraftable;
//    boolean isGatherable;
}
