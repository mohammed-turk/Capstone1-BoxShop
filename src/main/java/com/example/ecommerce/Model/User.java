package com.example.ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "id can not be empty")
    private String id;

    @NotEmpty(message = "username can not be empty")
    @Size(min = 6, message = "length has to be 6 or more")
    private String username;

    @NotEmpty
    @Size(min = 7,message = "length has to be 7 or more" )
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "must contain letters and digits")
    private String password;

    @NotEmpty(message = "email can not be empty")
    @Email
    private String email;
    @NotEmpty(message = "role can not be empty")
    @Pattern(regexp = "Admin|Customer", message = "role must be Admin or Customer")
    private String role;

    @NotNull(message = "balance can not be empty")
    @Positive(message = "balance has to be positive")
    private double balance;


}
