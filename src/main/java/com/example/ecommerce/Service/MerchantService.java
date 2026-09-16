package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class MerchantService {
    ArrayList<Merchant> Merchants = new ArrayList<>();

    public ArrayList<Merchant> getMerchants(){
        return Merchants;
    }

    public void postMerchant( Merchant merchant){
        Merchants.add(merchant);
    }

    public boolean putMerchant( String id, Merchant merchant){
        for (int i = 0; i< Merchants.size(); i++ ){
            if (Merchants.get(i).getId().equals(id)){
                Merchants.set(i, merchant);
                return true;
            }
        }
        return  false;
    }

    public boolean deleteMerchant( String id){
        for (int i = 0; i< Merchants.size(); i++ ){
            if (Merchants.get(i).getId().equals(id)){
                Merchants.remove(i);
                return true;
            }
        }
        return  false;
    }

}
