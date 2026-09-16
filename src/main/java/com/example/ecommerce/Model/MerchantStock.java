package com.example.ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "id can not be empty")
    private String id;

    @NotEmpty(message = "product id can not be empty")
    private String productId;

    @NotEmpty(message = "merchant id can not be empty")
    private String merchantId;

//    have to be 10 at the start
    @NotNull(message = "stock can not be null")
    private int stock;
}
