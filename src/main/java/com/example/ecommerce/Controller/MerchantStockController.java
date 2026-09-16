package com.example.ecommerce.Controller;

import com.example.ecommerce.API.APIResponse;
import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {
    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStocks(){
        ArrayList<MerchantStock> merchantStocks = merchantStockService.getMerchantStocks();
        return ResponseEntity.status(200).body(merchantStocks);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        int result = merchantStockService.postMerchantStock(merchantStock);

        return switch (result) {
            case 3 -> ResponseEntity.status(200).body(new APIResponse("added successfully"));
            case 1 -> ResponseEntity.status(400).body(new APIResponse("merchant not found"));
            case 0 -> ResponseEntity.status(400).body(new APIResponse("product not found"));
            case 2 -> ResponseEntity.status(400).body(new APIResponse("both merchant and product not found"));
            case 4 -> ResponseEntity.status(400).body(new APIResponse("stock must be more than 10 at the start"));
            default -> ResponseEntity.status(500).body(new APIResponse("unexpected error"));
        };

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> putMerchantStock(@PathVariable String id,@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        int result = merchantStockService.putMerchantStock(id, merchantStock);

        return switch (result) {
            case 3 -> ResponseEntity.status(200).body(new APIResponse("updated successfully"));
            case 2 -> ResponseEntity.status(400).body(new APIResponse("both merchant and product do not exist"));
            case 1 -> ResponseEntity.status(400).body(new APIResponse("merchant does not exist"));
            case 0 -> ResponseEntity.status(400).body(new APIResponse("product does not exist"));
            case -1 -> ResponseEntity.status(400).body(new APIResponse("no such merchant stock exists"));
            default -> ResponseEntity.status(500).body(new APIResponse("unexpected error"));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String id){
        boolean isFine = merchantStockService.deleteMerchantStock(id);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("deleted successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such merchant stock exist"));

    }

    @PutMapping("/add-stock/{productId}/{merchantId}/{amount}")
    public ResponseEntity<?> addStocks(@PathVariable String productId,
                                       @PathVariable String merchantId,
                                       @PathVariable int amount) {
        int result = merchantStockService.addStocks(productId, merchantId, amount);

        return switch (result) {
            case 1 -> ResponseEntity.status(200).body(new APIResponse("stock added successfully"));
            case -1 -> ResponseEntity.status(400).body(new APIResponse("amount must be greater than 0"));
            case 0 -> ResponseEntity.status(400).body(new APIResponse("merchant stock not found"));
            default -> ResponseEntity.status(500).body(new APIResponse("unexpected error"));
        };
    }

    @PutMapping("/apply-discount/{merchantId}/{discountPercentage}")
    public ResponseEntity<?> applyMerchantDiscount(@PathVariable String merchantId,
                                                   @PathVariable double discountPercentage) {
        int result = merchantStockService.applyMerchantDiscount(merchantId, discountPercentage);

        return switch (result) {
            case 1 -> ResponseEntity.status(200).body(new APIResponse("discount applied successfully"));
            case -1 -> ResponseEntity.status(400).body(new APIResponse("discount percentage must be between 1 and 100"));
            case 0 -> ResponseEntity.status(400).body(new APIResponse("merchant not found"));
            case 2 -> ResponseEntity.status(400).body(new APIResponse("no products found under this merchant"));
            default -> ResponseEntity.status(500).body(new APIResponse("unexpected error"));
        };
    }

    @PutMapping("/restock-risk/{merchantId}/{threshold}/{amount}")
    public ResponseEntity<?> restockLowInventory(@PathVariable String merchantId,
                                                 @PathVariable int threshold,
                                                 @PathVariable int amount) {
        int result = merchantStockService.restockLowInventory(merchantId, threshold, amount);

        if (result == -1) {
            return ResponseEntity.status(400).body(new APIResponse("threshold cannot be negative and amount must be greater than 0"));
        }
        if (result == 0) {
            return ResponseEntity.status(400).body(new APIResponse("merchant not found or no low-stock items needed restocking"));
        }

        return ResponseEntity.status(200).body(new APIResponse("successfully restocked " + result + " item(s)"));
    }


}
