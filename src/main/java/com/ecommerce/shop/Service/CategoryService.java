package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.ProductCategoriesModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductCategoriesModel> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<ProductCategoriesModel> findById(Long productId) {
        return categoryRepository.findById(productId);
    }

    public void addNewCategory(ProductCategoriesModel category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long categoryId, String name, List<ProductModel> products) {
        ProductCategoriesModel categoryModel = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException(
                        "Category with ID " + categoryId + "does not exist!"
                ));

        categoryModel.setName(name);
        categoryModel.setProducts(products);
    }

    public void deleteCategory(Long categoryId) {
        boolean exists = categoryRepository.existsById(categoryId);
        if (!exists) {
            throw new IllegalStateException(
                    "Category with ID " + categoryId + "does not exist!"
            );
        }

        categoryRepository.deleteById(categoryId);
    }

}
