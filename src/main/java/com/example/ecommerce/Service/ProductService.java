package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryService categoryService;
    ArrayList<Product> products = new ArrayList<>();

    public ArrayList<Product> getProducts(){
        return products;
    }

    public boolean postProduct(Product product){
        for (Category category: categoryService.getCategories()){
            if (category.getId().equals(product.getCategoryId())){
                products.add(product);
                return true;
            }
        }
        return false;
    }

    public boolean putProduct( String id, Product product){
        boolean categoryExists = false;
        for (Category category : categoryService.getCategories()) {
            if (category.getId().equals(product.getCategoryId())) {
                categoryExists = true;
                break;
            }
        }
        if (!categoryExists) {
            return false;
        }
        for (int i = 0; i< products.size(); i++ ){
            if (products.get(i).getId().equals(id)){
                products.set(i, product);
                return true;
            }
        }
        return  false;
    }

    public boolean deleteProduct( String id){
        for (int i = 0; i< products.size(); i++ ){
            if (products.get(i).getId().equals(id)){
                products.remove(i);
                return true;
            }
        }
        return  false;
    }

    public ArrayList<Product> filterByCategoryAndPrice(String categoryId, double minPrice, double maxPrice) {
        boolean categoryExists = false;
        for (Category category : categoryService.getCategories()) {
            if (category.getId().equals(categoryId)) {
                categoryExists = true;
                break;
            }
        }

        if (!categoryExists) {
            return null; // Category does not exist
        }

        ArrayList<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategoryId().equals(categoryId) &&
                    product.getPrice() >= minPrice &&
                    product.getPrice() <= maxPrice) {
                filtered.add(product);
            }
        }
        return filtered;
    }

}
