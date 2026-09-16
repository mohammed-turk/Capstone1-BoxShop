package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Model.Merchant;
import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {
    private final MerchantService merchantService;
    private final ProductService productService;
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    public ArrayList<MerchantStock> getMerchantStocks(){
        return merchantStocks;
    }

    public Integer postMerchantStock(MerchantStock merchantStock){
        boolean validMerchantId = false;
        boolean validProductId = false;
        for (Merchant merchant: merchantService.getMerchants()){
            if (merchant.getId().equals(merchantStock.getMerchantId())){
                validMerchantId = true;
                break;
            }
        }
        for (Product product: productService.getProducts()){
            if (product.getId().equals(merchantStock.getProductId())){
                validProductId = true;
                break;
            }
        }
        if (merchantStock.getStock() <11)
            return 4;

        if (validMerchantId && validProductId){
            merchantStocks.add(merchantStock);
            return 3;
        }
        if (!validMerchantId && !validProductId)
            return 2;
        if (!validMerchantId)
            return 1;

        return 0;
    }

    public Integer putMerchantStock( String id, MerchantStock merchantStock){
        int targetIndex = -1;
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            return -1;
        }

        boolean validMerchantId = false;
        for (Merchant merchant : merchantService.getMerchants()) {
            if (merchant.getId().equals(merchantStock.getMerchantId())) {
                validMerchantId = true;
                break;
            }
        }

        boolean validProductId = false;
        for (Product product : productService.getProducts()) {
            if (product.getId().equals(merchantStock.getProductId())) {
                validProductId = true;
                break;
            }
        }

        if (validMerchantId && validProductId) {
            merchantStock.setId(id);
            merchantStocks.set(targetIndex, merchantStock);
            return 3;
        }

        if (!validMerchantId && !validProductId) return 2;
        if (!validMerchantId) return 1;
        return 0;
    }

    public boolean deleteMerchantStock( String id){
        for (int i = 0; i< merchantStocks.size(); i++ ){
            if (merchantStocks.get(i).getId().equals(id)){
                merchantStocks.remove(i);
                return true;
            }
        }
        return  false;
    }

    public Integer addStocks( String productId, String merchantId, int amount){

        if (amount <= 0) {
            return -1;
        }
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getProductId().equals(productId) && merchantStock.getMerchantId().equals(merchantId)) {
                merchantStock.setStock(merchantStock.getStock() + amount);
                return 1;
            }
        }
        return 0;
    }

    public int applyMerchantDiscount(String merchantId, double discountPercentage) {
        if (discountPercentage <= 0 || discountPercentage > 100) {
            return -1;
        }

        boolean merchantExists = false;
        for (Merchant merchant : merchantService.getMerchants()) {
            if (merchant.getId().equals(merchantId)) {
                merchantExists = true;
                break;
            }
        }
        if (!merchantExists) return 0;

        boolean updatedAny = false;
        for (MerchantStock stock : merchantStocks) {
            if (stock.getMerchantId().equals(merchantId)) {
                for (Product product : productService.getProducts()) {
                    if (product.getId().equals(stock.getProductId())) {
                        double newPrice = product.getPrice() - (product.getPrice() * discountPercentage / 100.0);
                        product.setPrice(newPrice);
                        updatedAny = true;
                        break;
                    }
                }
            }
        }

        return updatedAny ? 1 : 2;
    }

    public int restockLowInventory(String merchantId, int threshold, int restockAmount) {
        if (threshold < 0 || restockAmount <= 0) {
            return -1;
        }

        boolean merchantExists = false;
        for (Merchant merchant : merchantService.getMerchants()) {
            if (merchant.getId().equals(merchantId)) {
                merchantExists = true;
                break;
            }
        }
        if (!merchantExists) return 0;

        int restockedCount = 0;
        for (MerchantStock stock : merchantStocks) {
            if (stock.getMerchantId().equals(merchantId) && stock.getStock() <= threshold) {
                stock.setStock(stock.getStock() + restockAmount);
                restockedCount++;
            }
        }

        return restockedCount;
    }

}
