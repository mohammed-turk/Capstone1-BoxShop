package com.example.ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "id can not be empty")
    private String id;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 4, message = "length have to be 4 or more")
    private String name;
}
