package com.example.ecommerce.Controller;

import com.example.ecommerce.API.APIResponse;
import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getProducts(){
        ArrayList<Product> products = productService.getProducts();
        return ResponseEntity.status(200).body(products);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postProducts(@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isFine = productService.postProduct(product);
        if (!isFine)
            return ResponseEntity.status(400).body(new APIResponse("no such category id exist"));

        return ResponseEntity.status(200).body(new APIResponse("added successfully"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> putProducts(@PathVariable String id,@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isFine = productService.putProduct(id, product);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("updated successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such product exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProducts(@PathVariable String id){
        boolean isFine = productService.deleteProduct(id);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("deleted successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such product exist"));

    }

    @GetMapping("/filter/{categoryId}/{minPrice}/{maxPrice}")
    public ResponseEntity<?> filterByCategoryAndPrice(@PathVariable String categoryId,
                                                      @PathVariable double minPrice,
                                                      @PathVariable double maxPrice) {
        if (minPrice < 0 || maxPrice < minPrice) {
            return ResponseEntity.status(400).body(new APIResponse("invalid price range"));
        }

        ArrayList<Product> result = productService.filterByCategoryAndPrice(categoryId, minPrice, maxPrice);

        if (result == null) {
            return ResponseEntity.status(400).body(new APIResponse("category not found"));
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new APIResponse("no products found in this category within the price range"));
        }

        return ResponseEntity.status(200).body(result);
    }
}
