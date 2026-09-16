package com.example.ecommerce.Controller;

import com.example.ecommerce.API.APIResponse;
import com.example.ecommerce.Model.Merchant;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Model.User;
import com.example.ecommerce.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        ArrayList<User> merchants = userService.getUsers();
        return ResponseEntity.status(200).body(merchants);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postUser(@RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        userService.postUser(user);
        return ResponseEntity.status(200).body(new APIResponse("added successfully"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> putUser(@PathVariable String id,@RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isFine = userService.putUser(id, user);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("updated successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such user exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        boolean isFine = userService.deleteUser(id);
        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("deleted successfully"));
        return ResponseEntity.status(400).body(new APIResponse("no such user exist"));

    }

    @PutMapping("/buy/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buy(@PathVariable String userId,
                                 @PathVariable String productId,
                                 @PathVariable String merchantId) {
        int result = userService.buy(userId, productId, merchantId);

        return switch (result) {
            case 5 -> ResponseEntity.status(200).body(new APIResponse("purchase completed successfully"));
            case 4 -> ResponseEntity.status(400).body(new APIResponse("insufficient balance"));
            case 3 -> ResponseEntity.status(400).body(new APIResponse("product is out of stock with this merchant"));
            case 2 -> ResponseEntity.status(400).body(new APIResponse("merchant not found"));
            case 1 -> ResponseEntity.status(400).body(new APIResponse("product not found"));
            case 0 -> ResponseEntity.status(400).body(new APIResponse("user not found"));
            default -> ResponseEntity.status(500).body(new APIResponse("unexpected error"));
        };
    }

    @PutMapping("/cancel-purchase/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> cancelPurchase(@PathVariable String userId,
                                            @PathVariable String productId,
                                            @PathVariable String merchantId) {
        int result = userService.cancelPurchase(userId, productId, merchantId);

        return switch (result) {
            case 5 -> ResponseEntity.status(200).body(new APIResponse("purchase canceled and refunded successfully"));
            case 3 -> ResponseEntity.status(400).body(new APIResponse("merchant stock record not found"));
            case 2 -> ResponseEntity.status(400).body(new APIResponse("merchant not found"));
            case 1 -> ResponseEntity.status(400).body(new APIResponse("product not found"));
            case 0 -> ResponseEntity.status(400).body(new APIResponse("user not found"));
            case -1 -> ResponseEntity.status(400).body(new APIResponse("user did not buy such a thing"));
            default -> ResponseEntity.status(500).body(new APIResponse("unexpected error"));
        };
    }

    @GetMapping("/recommend/{userId}/{categoryId}")
    public ResponseEntity<?> getAffordableProductsByCategory(@PathVariable String userId,
                                                             @PathVariable String categoryId) {
        ArrayList<Product> result = userService.getAffordableProductsByCategory(userId, categoryId);

        if (result == null) {
            return ResponseEntity.status(400).body(new APIResponse("user not found"));
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new APIResponse("no affordable products found in this category"));
        }

        return ResponseEntity.status(200).body(result);
    }

}
