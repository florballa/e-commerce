package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.ProductCategoriesModel;
import com.ecommerce.shop.Model.ProductModel;
import com.ecommerce.shop.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ModelAndView findAll(Model model) {
        List<ProductCategoriesModel> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list_categories");
        return modelAndView;
    }

    @GetMapping(path = "get/{categoryId}")
    public Optional<ProductCategoriesModel> findById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @GetMapping("/addCategoryTemplate")
    public ModelAndView saveCategoryTemplate(ProductCategoriesModel category, Model model) {

        model.addAttribute("category", category);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("save_category");

        return modelAndView;
    }

    @GetMapping("/updateCategoryTemplate/{categoryId}")
    public ModelAndView updateCategoryTemplate(@PathVariable("categoryId") Long categoryId, Model model) {

        Optional<ProductCategoriesModel> category = findById(categoryId);

        model.addAttribute("category", category);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("save_category");

        return modelAndView;
    }

    @PostMapping("/addCategory")
    public RedirectView addCategory(ProductCategoriesModel category) {
        categoryService.addNewCategory(category);

        return new RedirectView("/categories");
    }

    @PutMapping(path = "{categoryId}")
    public void updateCategory(@PathVariable("categoryId") Long categoryId,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) List<ProductModel> products) {

        categoryService.updateCategory(categoryId, name, products);

    }

    @DeleteMapping(path = "/delete/{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }


}
