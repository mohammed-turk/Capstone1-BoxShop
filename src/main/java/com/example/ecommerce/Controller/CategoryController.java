package com.example.ecommerce.Controller;

import com.example.ecommerce.API.APIResponse;
import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategories(){
        ArrayList<Category> categories = categoryService.getCategories();
        return ResponseEntity.status(200).body(categories);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postCategories(@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        categoryService.postCategory(category);
        return ResponseEntity.status(200).body(new APIResponse("added successfully"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> putCategory(@PathVariable String id,@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isFine = categoryService.putCategory(id, category);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("updated successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such category exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id){
        boolean isFine = categoryService.deleteCategory(id);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("deleted successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such category exist"));
    }


}
