package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Merchant;
import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ProductService productService;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;
    ArrayList<String> buyers = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers(){
        return users;
    }

    public void postUser( User user){
        users.add(user);
    }

    public boolean putUser( String id, User user){
        for (int i = 0; i< users.size(); i++ ){
            if (users.get(i).getId().equals(id)){
                users.set(i, user);
                return true;
            }
        }
        return  false;
    }

    public boolean deleteUser( String id){
        for (int i = 0; i< users.size(); i++ ){
            if (users.get(i).getId().equals(id)){
                users.remove(i);
                return true;
            }
        }
        return  false;
    }

    public Integer buy(String userId, String productId, String merchantId){
        boolean validUserId = false;
        boolean validProductId = false;
        boolean validMerchantId = false;
        int userIndex = 0;
        int productIndex =  0;

        for (User user: users){
            if (user.getId().equals(userId)){
                validUserId = true;
                break;
            }
            userIndex++;
        }

        for (Product product: productService.getProducts()){
            if (product.getId().equals(productId)){
                validProductId = true;
                break;
            }
            productIndex++;
        }

        for (Merchant merchant: merchantService.getMerchants()){
            if (merchant.getId().equals(merchantId)){
                validMerchantId = true;
                break;
            }
        }

        if (!validUserId)
            return 0;
        if (!validProductId)
            return 1;
        if (!validMerchantId)
            return 2;

        MerchantStock targetStock = null;
        for (MerchantStock merchantStock: merchantStockService.getMerchantStocks()){
            if (merchantStock.getMerchantId().equals(merchantId) &&
                    merchantStock.getProductId().equals(productId) &&
                    merchantStock.getStock() > 0){
                targetStock = merchantStock;
                break;
            }
        }

        if (targetStock == null)
            return 3;

        double productPrice = productService.getProducts().get(productIndex).getPrice();
        double userBalance = users.get(userIndex).getBalance();

        if (userBalance < productPrice)
            return 4;
        users.get(userIndex).setBalance(userBalance - productPrice);
        targetStock.setStock(targetStock.getStock() - 1);
        buyers.add(userId + "|" + productId + "|" + merchantId);

        return 5;



    }

    public Integer cancelPurchase(String userId, String productId, String merchantId){
        boolean validUserId = false;
        boolean validProductId = false;
        boolean validMerchantId = false;
        int userIndex = 0;
        int productIndex =  0;
        int buyerIndex = -1;

        boolean validBuyer = false;
        for (int i =0; i< buyers.size(); i++){
            if (buyers.get(i).equals(userId + "|" + productId + "|" + merchantId)){
                validBuyer = true;
                buyerIndex = i;
            }
        }

        if (!validBuyer)
            return -1;

        for (User user: users){
            if (user.getId().equals(userId)){
                validUserId = true;
                break;
            }
            userIndex++;
        }

        for (Product product: productService.getProducts()){
            if (product.getId().equals(productId)){
                validProductId = true;
                break;
            }
            productIndex++;
        }

        for (Merchant merchant: merchantService.getMerchants()){
            if (merchant.getId().equals(merchantId)){
                validMerchantId = true;
                break;
            }
        }

        if (!validUserId)
            return 0;
        if (!validProductId)
            return 1;
        if (!validMerchantId)
            return 2;

        MerchantStock targetStock = null;
        for (MerchantStock merchantStock: merchantStockService.getMerchantStocks()){
            if (merchantStock.getMerchantId().equals(merchantId) &&
                    merchantStock.getProductId().equals(productId)){
                targetStock = merchantStock;
                break;
            }
        }

        if (targetStock == null)
            return 3;

        double productPrice = productService.getProducts().get(productIndex).getPrice();
        double userBalance = users.get(userIndex).getBalance();


        users.get(userIndex).setBalance(userBalance + productPrice);
        targetStock.setStock(targetStock.getStock() + 1);
        buyers.remove(buyerIndex);
        return 5;
    }

    public ArrayList<Product> getAffordableProductsByCategory(String userId, String categoryId) {
        User targetUser = null;
        for (User user : users) {
            if (user.getId().equals(userId)) {
                targetUser = user;
                break;
            }
        }
        if (targetUser == null) {
            return null;
        }

        ArrayList<Product> affordable = new ArrayList<>();
        for (Product product : productService.getProducts()) {
            if (product.getCategoryId().equals(categoryId) && product.getPrice() <= targetUser.getBalance()) {
                affordable.add(product);
            }
        }
        return affordable;
    }
}
