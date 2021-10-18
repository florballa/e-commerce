package com.ecommerce.shop.Service;

import com.ecommerce.shop.Model.ProductCategoriesModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Repository.PaginationAndSorting.ProductPaginationAndSorting;
import com.ecommerce.shop.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPaginationAndSorting productPaging;

    public List<ProductModel> findAll(){
        return productRepository.findAll();
    }

    public List<ProductModel> getAllProducts(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<ProductModel> pagedResult = productPaging.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<ProductModel>();
        }
    }

    public Optional<ProductModel> findById(Long productId){
        return productRepository.findById(productId);
    }

    public void addNewProduct(ProductModel product){
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, String name, BigDecimal default_price, int stock, String description, ProductCategoriesModel categories){
        ProductModel productModel = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "Product with ID " + productId + "does not exist!"
                ));
        productModel.setName(name);
        productModel.setDefaultPrice(default_price);
        productModel.setStock(stock);
        productModel.setDescription(description);
        productModel.setCategories(categories);
    }

    public void deleteProduct(Long productId){
        boolean exists = productRepository.existsById(productId);
        if (!exists){
            throw new IllegalStateException(
                    "Product with ID " + productId + "does not exist!"
            );
        }

        productRepository.deleteById(productId);
    }

}
