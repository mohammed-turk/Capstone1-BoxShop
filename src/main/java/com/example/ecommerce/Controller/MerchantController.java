package com.example.ecommerce.Controller;

import com.example.ecommerce.API.APIResponse;
import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Model.Merchant;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchants(){
        ArrayList<Merchant> merchants = merchantService.getMerchants();
        return ResponseEntity.status(200).body(merchants);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postMerchant(@RequestBody @Valid Merchant merchant, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        merchantService.postMerchant(merchant);
        return ResponseEntity.status(200).body(new APIResponse("added successfully"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> putMerchant(@PathVariable String id,@RequestBody @Valid Merchant merchant, Errors errors){
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isFine = merchantService.putMerchant(id, merchant);

        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("updated successfully"));

        return ResponseEntity.status(400).body(new APIResponse("no such merchant exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String id){
        boolean isFine = merchantService.deleteMerchant(id);
        if (isFine)
            return ResponseEntity.status(200).body(new APIResponse("deleted successfully"));
        return ResponseEntity.status(400).body(new APIResponse("no such merchant exist"));

    }


}
