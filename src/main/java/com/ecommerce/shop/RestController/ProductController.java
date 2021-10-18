package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.ProductCategoriesModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Service.CategoryService;
import com.ecommerce.shop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ModelAndView findAll(Model model,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "id") String sortBy) {

        List<ProductModel> products = productService.getAllProducts(pageNo, pageSize, sortBy);
        model.addAttribute("products", products);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping(path = "get/{productId}")
    public Optional<ProductModel> findById(@PathVariable("productId") Long productId) {
        return productService.findById(productId);
    }

    @GetMapping("/addProductTemplate")
    public ModelAndView saveProductTemplate(ProductModel product, Model model) {

        List<ProductCategoriesModel> categoriesList = categoryService.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categoriesList", categoriesList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("save_product");

        return modelAndView;
    }

    @GetMapping("/updateProductTemplate/{productId}")
    public ModelAndView updateProductTemplate(@PathVariable("productId") Long productId, Model model) {

        Optional<ProductModel> product = findById(productId);
        List<ProductCategoriesModel> categoriesList = categoryService.findAll();

        model.addAttribute("product", product.get());
        model.addAttribute("categoriesList", categoriesList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("save_product");

        return modelAndView;
    }

    @PostMapping("/addProduct")
    public RedirectView addProduct(ProductModel product, Model model) {

        List<ProductCategoriesModel> categoriesList = categoryService.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categoriesList", categoriesList);

        productService.addNewProduct(product);

        return new RedirectView("/products");
    }

    @PutMapping("/update/{productId}")
    public void updateProduct(@PathVariable("productId") Long productId,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) BigDecimal default_price,
                              @RequestParam(required = false) int stock,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) ProductCategoriesModel categories) {

        productService.updateProduct(productId, name, default_price, stock, description, categories);

    }

    @DeleteMapping(path = "/delete/{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

}
