package com.example.ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
public class Category {

    @NotEmpty(message = "id can not be empty")
    private String id;
    @NotEmpty
    @Size(min = 4, message = "length should be more than 3")
    private String name;
}
