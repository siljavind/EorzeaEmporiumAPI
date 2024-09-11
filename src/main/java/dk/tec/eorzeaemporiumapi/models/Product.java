package dk.tec.eorzeaemporiumapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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
    String description;

    @PositiveOrZero
    float price;

//    @NotNull
//    String imagePath;

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
