package com.example.ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "id can not be empty")
    private String id;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 4, message = "length have to be 4 or more")
    private String name;

    @NotNull(message = "price can not be null")
    @Positive(message = "price should be positive")
    private double price;

//    validation required here
    @NotEmpty(message = "category id can not be empty")
    private String categoryId;
}
