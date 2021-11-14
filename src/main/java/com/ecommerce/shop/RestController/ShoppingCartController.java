package com.ecommerce.shop.RestController;

import com.ecommerce.shop.Model.CartItemModel;
import com.ecommerce.shop.Model.UserModel;
import com.ecommerce.shop.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shopping/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService ss;

    @GetMapping
    public ResponseEntity<List<CartItemModel>> showShoppingCart(@AuthenticationPrincipal UserModel user) {

        List<CartItemModel> cartItems = ss.listCartItems(user);

        return ResponseEntity.ok().body(cartItems);
    }

    @PostMapping("/add/{productId}/{quantity}")
    public ResponseEntity<String> addProductToCart(@PathVariable("productId") Long productId,
                                                   @PathVariable("quantity") Integer quantity,
                                                   @AuthenticationPrincipal UserModel user) {

        Integer addedQuantity = ss.addProduct(productId, quantity, user);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/shopping/cart/add/{productId}/{quantity}").toUriString());
        return ResponseEntity.created(uri).body(addedQuantity + "added item(s)");
    }

    @PostMapping("/update/{productId}/{quantity}")
    public ResponseEntity<String> updateQuantity(@PathVariable("productId") Long productId,
                                                 @PathVariable("quantity") Integer quantity,
                                                 @AuthenticationPrincipal UserModel user) {

        BigDecimal subtotal = ss.updateQuantity(quantity, productId, user.getId());

        return ResponseEntity.ok().body(String.valueOf(subtotal));
    }

    @PostMapping("/remove/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable("productId") Long productId,
                                                        @AuthenticationPrincipal UserModel user) {
        ss.removeProductFromCart(user.getId(), productId);
        return ResponseEntity.ok().body("Product has been removed from your shopping cart!");
    }
}
