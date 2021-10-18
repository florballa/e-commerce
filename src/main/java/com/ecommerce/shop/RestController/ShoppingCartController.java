package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.CartItemModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shopping/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService ss;

    @GetMapping
    public ModelAndView showShoppingCart(Model model,
                                         @AuthenticationPrincipal UserModel user) {

        List<CartItemModel> cartItems = ss.listCartItems(user);

        model.addAttribute("cartItems", cartItems);
//        model.addAttribute("pageTitle", "Shopping Cart");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shopping_cart");
        return modelAndView;
    }

    @PostMapping("/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Long productId,
                                   @PathVariable("quantity") Integer quantity,
                                   @AuthenticationPrincipal UserModel user) {

        Integer addedQuantity = ss.addProduct(productId, quantity, user);

        return addedQuantity + "item(s) of this product were added to the cart!";

    }

    @PostMapping("/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Long productId,
                                 @PathVariable("quantity") Integer quantity,
                                 @AuthenticationPrincipal UserModel user) {

        BigDecimal subtotal = ss.updateQuantity(quantity, productId, user.getId());

        return String.valueOf(subtotal);

    }

    @PostMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable("productId") Long productId,
                                        @AuthenticationPrincipal UserModel user) {

        ss.removeProductFromCart(user.getId(), productId);

        return "Product has been removed from your shopping cart!";

    }
}
